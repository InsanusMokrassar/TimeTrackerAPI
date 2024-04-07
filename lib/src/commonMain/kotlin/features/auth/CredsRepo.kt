package dev.inmo.time_tracker.lib.features.auth

import dev.inmo.time_tracker.common.models.auth.Credentials

interface CredsRepo {
    suspend fun saveCreds(creds: Credentials.Token)
    suspend fun clearCreds()
    suspend fun getCreds(): Credentials.Token?
}