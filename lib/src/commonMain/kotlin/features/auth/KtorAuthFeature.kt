package dev.inmo.time_tracker.lib.features.auth

import dev.inmo.kslog.common.d
import dev.inmo.kslog.common.logger
import dev.inmo.micro_utils.common.Either
import dev.inmo.micro_utils.coroutines.SmartRWLocker
import dev.inmo.micro_utils.coroutines.launchSafelyWithoutExceptions
import dev.inmo.micro_utils.coroutines.withReadAcquire
import dev.inmo.micro_utils.coroutines.withWriteLock
import dev.inmo.time_tracker.common.models.auth.Credentials
import dev.inmo.time_tracker.common.models.user.RegisteredUser
import dev.inmo.time_tracker.common.models.user.Username
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private object RequireRerequestRefreshException : Exception()

class KtorAuthFeature(
    private val credsRepo: CredsRepo,
    private val baseUrl: String,
    baseClient: HttpClient,
    private val scope: CoroutineScope
) : AuthFeature {
    private val Log = logger
    private val requestsLocker = SmartRWLocker()
    private val anyAuthUrlPart = "$baseUrl/${AuthConstants.authRootPathPart}"
    private val refreshUrl = "$baseUrl/${AuthConstants.authRootPathPart}/${AuthConstants.refreshPathPart}"
    private val loginUrl = "$baseUrl/${AuthConstants.authRootPathPart}/${AuthConstants.loginPathPart}"

    internal val resultClient = baseClient.apply {
        plugin(HttpSend).intercept { request ->
            var serverAvailability = true
            var resultCall: HttpClientCall? = null
            val urlString = request.url.toString()
            while (resultCall == null) {
                if (requestsLocker.writeMutex.isLocked && (urlString.contains(anyAuthUrlPart))) {
                    Log.d { "$urlString is some blocking auth request, pass response as is" }
                    return@intercept execute(request)
                }
                resultCall = requestsLocker.withReadAcquire {
                    val token = credsRepo.getCreds()
                    if (token != null) {
                        request.header(HttpHeaders.Authorization, token.token)
                    }

                    val originalCall = execute(request)
                    val httpResponse = originalCall.response
                    val httpRequest = originalCall.request

                    val url = originalCall.request.url.toString()

                    val apiUrlGetterValue = baseUrl

                    when {

                        url.replace(apiUrlGetterValue, "") == url -> {
                            Log.d { "For response $httpResponse APIUrlGetter returned url not equal to the url with new base url, throwing an error" }
                            null
                        }

                        httpResponse.status.value > HttpStatusCode.OK.value && httpResponse.status.value < HttpStatusCode.MultipleChoices.value -> {
                            Log.d { "$httpResponse is ok or some other success answer, pass as is" }
                            null
                        }

                        url.contains("${baseUrl}/${AuthConstants.authRootPathPart}") -> {
                            Log.d { "$url is some auth request, pass response as is" }
                            null
                        }

                        httpResponse.status.value >= 500 -> {
                            if (url.contains("${baseUrl}/${AuthConstants.authRootPathPart}/${AuthConstants.refreshPathPart}")) {
                                throw RequireRerequestRefreshException
                            }
                            Log.d { "$httpResponse is server error response, retry" }
                            serverAvailability = when (httpResponse.status) {
                                HttpStatusCode.BadGateway,
                                HttpStatusCode.ServiceUnavailable,
                                HttpStatusCode.GatewayTimeout -> false

                                else -> true
                            }
                            originalCall
                        }

                        httpResponse.status != HttpStatusCode.Unauthorized -> {
                            Log.d { "$httpResponse is not unauthorized response, pass response as is" }
                            null
                        }

                        !httpRequest.headers.contains(HttpHeaders.Authorization) && token != null -> {
                            Log.d { "$httpResponse is unauthorized, but request contains not token when token is presented locally" }
                            null
                        }

                        else -> {
                            Log.d { "$httpResponse is unauthorized response, retry" }
                            triggerRefreshTokenJob()
                            originalCall
                        }
                    }
                }
            }

            resultCall
        }
    }

    private var refreshTokenJob: Job? = null
    private val refreshTokenMutex = Mutex()
    private fun triggerRefreshTokenJob() {
        scope.launchSafelyWithoutExceptions {
            refreshTokenMutex.withLock {
                if (refreshTokenJob == null) {
                    refreshTokenJob = scope.launchSafelyWithoutExceptions {
                        requestsLocker.withWriteLock {
                            val token = credsRepo.getCreds() ?: return@launchSafelyWithoutExceptions
                            val response = resultClient.post(
                                refreshUrl
                            ) {
                                setBody(token.refresh.string)
                            }.body<Credentials.Token?>()
                            if (response != null) {
                                credsRepo.saveCreds(response)
                            } else {
                                credsRepo.clearCreds()
                            }
                        }

                        refreshTokenMutex.withLock {
                            refreshTokenJob = null
                        }
                    }
                }
            }
        }
    }

    override suspend fun getMe(): RegisteredUser? {

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