// THIS CODE HAVE BEEN GENERATED AUTOMATICALLY
// TO REGENERATE IT JUST DELETE FILE
// ORIGINAL FILE: Timer.kt
package dev.inmo.time_tracker.common.models.timers

import dev.inmo.time_tracker.common.models.acitivties.ActivityId
import dev.inmo.time_tracker.common.utils.DateTimeSerializer
import korlibs.time.DateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(value = "NewTimer")
public data class NewTimer(
  override val activity: ActivityId,
  @Serializable(with = DateTimeSerializer::class)
  override val start: DateTime,
  @Serializable(with = DateTimeSerializer::class)
  override val end: DateTime,
) : Timer

@Serializable
@SerialName(value = "RegisteredTimer")
public data class RegisteredTimer(
  override val id: TimerId,
  override val activity: ActivityId,
  @Serializable(with = DateTimeSerializer::class)
  override val start: DateTime,
  @Serializable(with = DateTimeSerializer::class)
  override val end: DateTime,
) : Timer, IRegisteredTimer

public fun Timer.asNew(): NewTimer = NewTimer(activity, start, end)

public fun Timer.asRegistered(id: TimerId): RegisteredTimer = RegisteredTimer(id, activity, start,
    end)
