package ru.terrakok.gitlabclient.model.auth

import io.reactivex.Completable
import ru.terrakok.gitlabclient.model.server.ServerConfig
import java.util.*

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
class AuthManager(
        private val serverConfig: ServerConfig,
        private val authRepository: AuthRepository) {

    private val hash = UUID.randomUUID().toString()

    val oauthUrl = "${serverConfig.SERVER_URL}oauth/authorize?client_id=${serverConfig.APP_ID}" +
            "&redirect_uri=${serverConfig.AUTH_REDIRECT_URI}&response_type=code&state=$hash"


    fun checkOAuthRedirect(url: String) = url.indexOf(serverConfig.AUTH_REDIRECT_URI) == 0

    fun isSignedIn() = authRepository.getSignState().firstOrError()

    fun login(oauthRedirect: String) =
            Completable.defer {
                if (oauthRedirect.contains(hash)) {
                    authRepository.refreshServerToken(
                                    serverConfig.APP_ID,
                                    serverConfig.APP_KEY,
                                    getCodeFromAuthRedirect(oauthRedirect),
                                    serverConfig.AUTH_REDIRECT_URI)
                } else {
                    Completable.error(RuntimeException("Not valid oauth hash!"))
                }
            }

    private fun getCodeFromAuthRedirect(url: String): String {
        val redirectCodeTag = "code="
        val redirectStateTag = "&state="
        val fi = url.indexOf(redirectCodeTag) + redirectCodeTag.length
        val li = url.indexOf(redirectStateTag)
        return url.substring(fi, li)
    }

    fun logout() = authRepository.clearToken()
}