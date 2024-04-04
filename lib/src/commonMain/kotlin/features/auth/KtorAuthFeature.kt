package dev.inmo.time_tracker.lib.features.auth

import dev.inmo.micro_utils.common.Either
import dev.inmo.time_tracker.common.models.user.RegisteredUser
import dev.inmo.time_tracker.common.models.user.Username
import io.ktor.client.*

class KtorAuthFeature(
    private val credsRepo: CredsRepo,
    baseClient: HttpClient
) : AuthFeature {
    internal val resultClient = baseClient.config {
        
    }

    override suspend fun getMe(): RegisteredUser? {
        TODO("Not yet implemented")
    }

    override suspend fun auth(username: Username, password: String): Either<RegisteredUser, AuthFeature.Error.Auth>? {
        TODO("Not yet implemented")
    }

    override suspend fun register(
        username: Username,
        password: String
    ): Either<RegisteredUser, AuthFeature.Error.Register>? {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): Boolean {
        TODO("Not yet implemented")
    }
}