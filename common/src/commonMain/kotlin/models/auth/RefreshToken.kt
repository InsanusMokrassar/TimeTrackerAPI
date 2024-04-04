package dev.inmo.time_tracker.common.models.auth

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class RefreshToken(
    val string: String
)
