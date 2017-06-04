package ru.terrakok.gitlabclient.model.interactor.auth

import android.support.annotation.VisibleForTesting
import io.reactivex.Completable
import ru.terrakok.gitlabclient.model.data.server.ServerConfig
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository
import java.util.*

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
class AuthInteractor(
        private val serverConfig: ServerConfig,
        private val authRepository: AuthRepository) {

    @VisibleForTesting
    val hash = UUID.randomUUID().toString()

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