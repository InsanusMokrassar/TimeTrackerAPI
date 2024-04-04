package dev.inmo.time_tracker.lib.models

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Username(val string: String)
