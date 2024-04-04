package dev.inmo.time_tracker.lib.features.auth

import dev.inmo.time_tracker.common.models.auth.Credentials

interface CredsRepo {
    suspend fun save(creds: Credentials.Token)
    suspend fun getCreds(): Credentials.Token?
}