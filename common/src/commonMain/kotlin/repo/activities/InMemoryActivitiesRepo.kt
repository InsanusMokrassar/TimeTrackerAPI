package dev.inmo.time_tracker.common.repo.activities

import dev.inmo.micro_utils.coroutines.SmartRWLocker
import dev.inmo.micro_utils.repos.MapCRUDRepo
import dev.inmo.time_tracker.common.models.acitivties.ActivityId
import dev.inmo.time_tracker.common.models.acitivties.NewActivity
import dev.inmo.time_tracker.common.models.acitivties.RegisteredActivity
import dev.inmo.time_tracker.common.models.acitivties.asRegistered

class InMemoryActivitiesRepo(
    map: MutableMap<ActivityId, RegisteredActivity> = mutableMapOf(),
    locker: SmartRWLocker = SmartRWLocker()
) : ActivitiesRepo,
    MapCRUDRepo<RegisteredActivity, ActivityId, NewActivity>(map, locker) {
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
}