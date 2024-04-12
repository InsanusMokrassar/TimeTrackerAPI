package dev.inmo.time_tracker.common.repo.timers

import dev.inmo.micro_utils.coroutines.SmartRWLocker
import dev.inmo.micro_utils.repos.MapCRUDRepo
import dev.inmo.time_tracker.common.models.timers.NewTimer
import dev.inmo.time_tracker.common.models.timers.RegisteredTimer
import dev.inmo.time_tracker.common.models.timers.TimerId
import dev.inmo.time_tracker.common.models.timers.asRegistered

class InMemoryTimersRepo(
    map: MutableMap<TimerId, RegisteredTimer> = mutableMapOf(),
    locker: SmartRWLocker = SmartRWLocker()
) : TimersRepo,
    MapCRUDRepo<RegisteredTimer, TimerId, NewTimer>(map, locker) {
    override suspend fun createObject(newValue: NewTimer): Pair<TimerId, RegisteredTimer> {
        val id = TimerId(map.size.toLong())
        return id to newValue.asRegistered(id)
    }

    override suspend fun updateObject(
        newValue: NewTimer,
        id: TimerId,
        old: RegisteredTimer
    ): RegisteredTimer {
        return newValue.asRegistered(id)
    }
}