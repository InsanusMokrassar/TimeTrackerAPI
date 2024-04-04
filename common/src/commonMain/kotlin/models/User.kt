package dev.inmo.time_tracker.common.models

import dev.inmo.micro_utils.repos.annotations.GenerateCRUDModel
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class UserId(val long: Long)

@Serializable
@JvmInline
value class Username(val string: String)

@Serializable
@GenerateCRUDModel(IRegisteredUser::class)
sealed interface User {
    val username: Username
}

sealed interface IRegisteredUser : User {
    val id: UserId
}