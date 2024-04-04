package dev.inmo.time_tracker.lib

import dev.inmo.time_tracker.lib.features.auth.AuthFeature

sealed interface TimeTrackerClient {
    val authFeature: AuthFeature
}