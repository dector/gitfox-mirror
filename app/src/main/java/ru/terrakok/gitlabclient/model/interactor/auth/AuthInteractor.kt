package ru.terrakok.gitlabclient.model.interactor.auth

import io.reactivex.Completable
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository
import ru.terrakok.gitlabclient.toothpick.qualifier.ServerPath
import java.net.URI
import java.util.*
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
class AuthInteractor(
        private val serverPath: String,
        private val authRepository: AuthRepository,
        private val hash: String) {

    @Inject constructor(
            @ServerPath serverPath: String,
            authRepository: AuthRepository
    ) : this(serverPath, authRepository, UUID.randomUUID().toString())

    private val PARAMETER_CODE = "code"

    //todo: before release change and move to private config
    private val OAUTH_APP_ID = "808b7f51c6634294afd879edd75d5eaf55f1a75e7fe5bd91ca8b7140a5af639d"
    private val OAUTH_APP_KEY = "a9dd39c8d2e781b65814007ca0f8b555d34f79b4d30c9356c38bb7ad9909c6f3"
    private val OAUTH_REDIRECT_URI = "app://gitlab.client/"

    val oauthUrl = "${serverPath}oauth/authorize?client_id=$OAUTH_APP_ID" +
            "&redirect_uri=$OAUTH_REDIRECT_URI&response_type=code&state=$hash"


    fun checkOAuthRedirect(url: String) = url.indexOf(OAUTH_REDIRECT_URI) == 0

    fun isSignedIn() = authRepository.getSignState().firstOrError()

    fun login(oauthRedirect: String) =
            Completable.defer {
                if (oauthRedirect.contains(hash)) {
                    authRepository
                            .requestOAuthToken(
                                    OAUTH_APP_ID,
                                    OAUTH_APP_KEY,
                                    getQueryParameterFromUri(oauthRedirect, PARAMETER_CODE),
                                    OAUTH_REDIRECT_URI
                            )
                            .doOnSuccess {
                                authRepository.saveAuthData(it.token, serverPath, true)
                            }
                            .toCompletable()
                } else {
                    Completable.error(RuntimeException("Not valid oauth hash!"))
                }
            }

    fun login(customServerPath: String, privateToken: String) = Completable.fromAction {
        var serverPath = customServerPath
        if (!customServerPath.endsWith("/")) serverPath += "/"

        authRepository.saveAuthData(privateToken, serverPath, false)
    }

    fun logout() = Completable.fromAction {
        authRepository.clearAuthData()
    }

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