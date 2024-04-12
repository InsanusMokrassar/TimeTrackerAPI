//package dev.inmo.time_tracker.common.models.auth
//
//import dev.inmo.time_tracker.common.models.user.Username
//import kotlinx.serialization.Serializable
//
//@Serializable
//sealed interface Credentials {
//    @Serializable
//    data class Basic(
//        val username: Username,
//        val password: String
//    ) : Credentials
//
//    @Serializable
//    data class Token(
//        val token: AuthToken,
//        val refresh: RefreshToken
//    )
//}