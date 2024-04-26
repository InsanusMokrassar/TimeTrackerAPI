package dev.inmo.time_tracker.common.repo.timers

import dev.inmo.micro_utils.common.MPPFile
import dev.inmo.micro_utils.common.applyDiff
import dev.inmo.micro_utils.coroutines.*
import dev.inmo.micro_utils.repos.KeyValueRepo
import dev.inmo.micro_utils.repos.MapKeyValueRepo
import dev.inmo.micro_utils.repos.cache.InvalidatableRepo
import dev.inmo.time_tracker.common.models.acitivties.ActivityId
import dev.inmo.time_tracker.common.models.timers.RegisteredTimer
import dev.inmo.time_tracker.common.utils.DateTimeSerializer
import korlibs.time.DateTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.merge
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class InFileStartedTimersRepo (
    private val targetFile: MPPFile,
    private val map: MutableMap<ActivityId, DateTime> = mutableMapOf(),
    private val locker: SmartRWLocker = SmartRWLocker(),
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    debounceMillis: Long = 13L,
    private val json: Json = Json
) : KeyValueRepo<ActivityId, DateTime> by MapKeyValueRepo<ActivityId, DateTime>(map, locker), InvalidatableRepo {
    val syncJob = merge(
        onNewValue,
        onValueRemoved
    ).let { if (debounceMillis > 0L)  it.debounce(debounceMillis) else it }.subscribeSafelyWithoutExceptions(scope) {
        targetFile.writeText(
            locker.withReadAcquire {
                json.encodeToString(mapSerializer, map)
            }
        )
    }

    init {
        scope.launchSafelyWithoutExceptions {
            invalidate()
        }
    }

    override suspend fun invalidate() {
        locker.withWriteLock {
            map.applyDiff(
                json.decodeFromString<Map<ActivityId, DateTime>>(
                    mapSerializer,
                    targetFile.readText()
                )
            )
        }
    }

    companion object {
        private val mapSerializer = MapSerializer(
            ActivityId.serializer(),
            DateTimeSerializer
        )
    }
}
