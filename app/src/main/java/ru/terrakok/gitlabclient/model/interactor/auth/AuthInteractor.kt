package ru.terrakok.gitlabclient.model.interactor.auth

import io.reactivex.Completable
import ru.terrakok.gitlabclient.model.data.server.ServerConfig
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository
import java.net.URI

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

    private fun getQueryParameterFromUri(url: String, queryName: String): String {
        val uri = URI(url)
        val query = uri.query
        val parameters = query.split("&")

        var code = ""
        for (parameter in parameters) {
            if (parameter.startsWith(queryName)) {
                code = parameter.substring(queryName.length + 1)
                break
            }
        }
        return code
    }

}