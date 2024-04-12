package dev.inmo.time_tracker.common.models.acitivties

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class ActivityId(
    val long: Long
)
