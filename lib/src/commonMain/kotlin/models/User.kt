package dev.inmo.time_tracker.lib.models

import dev.inmo.micro_utils.repos.annotations.GenerateCRUDModel
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class UserId(val long: Long)

@Serializable
@GenerateCRUDModel(IRegisteredUser::class)
sealed interface User {
    val username: Username
}

sealed interface IRegisteredUser : User {
    val id: UserId
}
