package dev.inmo.time_tracker.common.repo.activities

import dev.inmo.micro_utils.common.MPPFile
import dev.inmo.micro_utils.common.applyDiff
import dev.inmo.micro_utils.coroutines.*
import dev.inmo.micro_utils.repos.MapCRUDRepo
import dev.inmo.micro_utils.repos.cache.InvalidatableRepo
import dev.inmo.micro_utils.repos.cache.util.ActualizeAllClearMode
import dev.inmo.time_tracker.common.models.acitivties.ActivityId
import dev.inmo.time_tracker.common.models.acitivties.NewActivity
import dev.inmo.time_tracker.common.models.acitivties.RegisteredActivity
import dev.inmo.time_tracker.common.models.acitivties.asRegistered
import dev.inmo.micro_utils.repos.cache.util.actualizeAll
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.merge
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class InFilesActivitiesRepo(
    private val targetFile: MPPFile,
    map: MutableMap<ActivityId, RegisteredActivity> = mutableMapOf(),
    locker: SmartRWLocker = SmartRWLocker(),
    scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    debounceMillis: Long = 13L,
    private val json: Json = Json
) : ActivitiesRepo,
    MapCRUDRepo<RegisteredActivity, ActivityId, NewActivity>(map, locker),
    InvalidatableRepo{
    val syncJob = merge(
        newObjectsFlow,
        updatedObjectsFlow,
        deletedObjectsIdsFlow
    ).let { if (debounceMillis > 0L)  it.debounce(debounceMillis) else it }.subscribeSafelyWithoutExceptions(scope) {
        targetFile.writeText(
            locker.withReadAcquire {
                json.encodeToString(map.values.toList())
            }
        )
    }

    init {
        scope.launchSafelyWithoutExceptions {
            invalidate()
        }
    }

    override suspend fun createObject(newValue: NewActivity): Pair<ActivityId, RegisteredActivity> {
        val id = ActivityId(map.size.toLong())
        return id to newValue.asRegistered(id)
    }

    override suspend fun updateObject(
        newValue: NewActivity,
        id: ActivityId,
        old: RegisteredActivity
    ): RegisteredActivity {
        return newValue.asRegistered(id)
    }

    override suspend fun invalidate() {
        locker.withWriteLock {
            map.applyDiff(
                json.decodeFromString<List<RegisteredActivity>>(
                    targetFile.readText()
                ).associateBy { it.id }
            )
        }
    }
}