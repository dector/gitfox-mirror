package ru.terrakok.gitlabclient.model.interactor.auth

import io.reactivex.Completable
import ru.terrakok.gitlabclient.model.data.server.ServerConfig
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository
import ru.terrakok.gitlabclient.model.utils.getQueryParameterFromUri

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
class AuthInteractor(
        private val serverConfig: ServerConfig,
        private val authRepository: AuthRepository,
        private val hash: String) {

    private val PARAMETER_CODE = "code"

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
                                    getQueryParameterFromUri(oauthRedirect, PARAMETER_CODE),
                                    serverConfig.AUTH_REDIRECT_URI)
                } else {
                    Completable.error(RuntimeException("Not valid oauth hash!"))
                }
            }

    fun logout() = authRepository.clearToken()
}