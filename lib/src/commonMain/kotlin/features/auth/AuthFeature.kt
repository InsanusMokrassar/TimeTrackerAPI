//package dev.inmo.time_tracker.lib.features.auth
//
//import dev.inmo.micro_utils.common.Either
//import dev.inmo.time_tracker.common.models.user.RegisteredUser
//import dev.inmo.time_tracker.common.models.user.Username
//
//interface AuthFeature {
//    sealed interface Error {
//        sealed interface Auth : Error
//        sealed interface Register : Error
//    }
//
//    /**
//     * @return User if current client has been authorized or null otherwise
//     */
//    suspend fun getMe(): RegisteredUser?
//
//    /**
//     * @param username Username of user
//     * @param password Plain password as is
//     *
//     * @return [Either] with [RegisteredUser] on successful authorization and [Either] with [Error.Auth] otherwise
//     */
//    suspend fun auth(username: Username, password: String): Either<RegisteredUser, Error.Auth>?
//
//    /**
//     * In case of successful registration there is no need to do [auth] manually
//     *
//     * @param username Username of user
//     * @param password Plain password as is
//     *
//     * @return [Either] with [RegisteredUser] on successful registration and [Either] with [Error.Auth] otherwise
//     */
//    suspend fun register(username: Username, password: String): Either<RegisteredUser, Error.Register>?
//
//    /**
//     * Logout on the server
//     */
//    suspend fun logout(): Boolean
//}