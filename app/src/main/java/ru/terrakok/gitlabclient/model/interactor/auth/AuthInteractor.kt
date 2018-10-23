package ru.terrakok.gitlabclient.model.interactor.auth

import io.reactivex.Completable
import ru.terrakok.gitlabclient.model.data.cache.ProjectCache
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.module.ServerModule
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultServerPath
import ru.terrakok.gitlabclient.toothpick.qualifier.ServerPath
import toothpick.Toothpick
import java.net.URI
import java.util.*
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
class AuthInteractor(
    private val serverPath: String,
    private val defaultServerPath: String,
    private val authRepository: AuthRepository,
    private val hash: String,
    private val oauthParams: OAuthParams,
    private val projectCache: ProjectCache
) {

    @Inject constructor(
        @ServerPath serverPath: String,
        @DefaultServerPath defaultServerPath: String,
        authRepository: AuthRepository,
        oauthParams: OAuthParams,
        projectCache: ProjectCache
    ) : this(
        serverPath,
        defaultServerPath,
        authRepository,
        UUID.randomUUID().toString(),
        oauthParams,
        projectCache
    )

    val oauthUrl = "${serverPath}oauth/authorize?client_id=${oauthParams.appId}" +
        "&redirect_uri=${oauthParams.redirectUrl}&response_type=code&state=$hash"


    fun checkOAuthRedirect(url: String) = url.indexOf(oauthParams.redirectUrl) == 0

    fun isSignedIn() = authRepository.isSignedIn

    fun login(oauthRedirect: String) =
        Completable.defer {
            if (oauthRedirect.contains(hash)) {
                authRepository
                    .requestOAuthToken(
                        oauthParams.appId,
                        oauthParams.appKey,
                        getQueryParameterFromUri(oauthRedirect, PARAMETER_CODE),
                        oauthParams.redirectUrl
                    )
                    .doOnSuccess {
                        authRepository.saveAuthData(it.token, serverPath, true)
                    }
                    .toCompletable()
            } else {
                Completable.error(RuntimeException("Not valid oauth hash!"))
            }
        }

    fun login(customServerPath: String, privateToken: String) =
        Completable.fromAction {
            var serverPath = customServerPath
            if (!customServerPath.endsWith("/")) serverPath += "/"

            authRepository.saveAuthData(privateToken, serverPath, false)
            switchServerIfNeeded(serverPath)
        }

    fun logout() {
        authRepository.clearAuthData()
        projectCache.clear()
        switchServerIfNeeded(defaultServerPath)
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

    private fun switchServerIfNeeded(newServerPath: String) {
        if (serverPath != newServerPath) {
            Toothpick.closeScope(DI.SERVER_SCOPE)
            Toothpick
                .openScopes(DI.APP_SCOPE, DI.SERVER_SCOPE)
                .installModules(ServerModule(newServerPath))
        }
    }

    companion object {
        private const val PARAMETER_CODE = "code"
    }
}