package dev.inmo.time_tracker.common.models.timers

import dev.inmo.micro_utils.repos.annotations.GenerateCRUDModel
import dev.inmo.micro_utils.repos.annotations.GenerateCRUDModelExcludeOverride
import dev.inmo.time_tracker.common.models.acitivties.ActivityId
import dev.inmo.time_tracker.common.utils.DateTimeSerializer
import korlibs.time.DateTime
import korlibs.time.TimeSpan
import kotlinx.serialization.Serializable

@Serializable
@GenerateCRUDModel(IRegisteredTimer::class)
sealed interface Timer {
    val activity: ActivityId
    @Serializable(DateTimeSerializer::class)
    val start: DateTime
    @Serializable(DateTimeSerializer::class)
    val end: DateTime

    @GenerateCRUDModelExcludeOverride
    val duration: TimeSpan
        get() = end - start
}
@Serializable
sealed interface IRegisteredTimer {
    val id: TimerId
}