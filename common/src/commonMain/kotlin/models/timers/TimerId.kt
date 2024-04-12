package dev.inmo.time_tracker.common.models.timers

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class TimerId(val long: Long)
