// THIS CODE HAVE BEEN GENERATED AUTOMATICALLY
// TO REGENERATE IT JUST DELETE FILE
// ORIGINAL FILE: Activity.kt
package dev.inmo.time_tracker.common.models.acitivties

import kotlin.String
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(value = "NewActivity")
public data class NewActivity(
  override val title: String,
) : Activity

@Serializable
@SerialName(value = "RegisteredActivity")
public data class RegisteredActivity(
  override val id: ActivityId,
  override val title: String,
) : Activity, IRegisteredActivity

public fun Activity.asNew(): NewActivity = NewActivity(title)

public fun Activity.asRegistered(id: ActivityId): RegisteredActivity = RegisteredActivity(id, title)
