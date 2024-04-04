// THIS CODE HAVE BEEN GENERATED AUTOMATICALLY
// TO REGENERATE IT JUST DELETE FILE
// ORIGINAL FILE: User.kt
package dev.inmo.time_tracker.common.models.user

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(value = "NewUser")
public data class NewUser(
    override val username: Username,
) : User

@Serializable
@SerialName(value = "RegisteredUser")
public data class RegisteredUser(
    override val id: UserId,
    override val username: Username,
) : User, IRegisteredUser

public fun User.asNew(): NewUser = NewUser(username)

public fun User.asRegistered(id: UserId): RegisteredUser = RegisteredUser(id, username)
