package ru.terrakok.gitlabclient.model.interactor.session

import io.reactivex.Completable
import ru.terrakok.gitlabclient.entity.app.session.OAuthParams
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.model.data.cache.ProjectCache
import ru.terrakok.gitlabclient.model.repository.session.SessionRepository
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.module.ServerModule
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultServerPath
import toothpick.Toothpick
import java.net.URI
import java.util.*
import javax.inject.Inject

class SessionInteractor @Inject constructor(
    @DefaultServerPath serverPath: String,
    private val sessionRepository: SessionRepository,
    private val oauthParams: OAuthParams,
    private val projectCache: ProjectCache
) {
    private val hash = UUID.randomUUID().toString()

    val oauthUrl = "${serverPath}oauth/authorize?client_id=${oauthParams.appId}" +
        "&redirect_uri=${oauthParams.redirectUrl}&response_type=code&state=$hash"

    fun checkOAuthRedirect(url: String) = url.indexOf(oauthParams.redirectUrl) == 0

    fun login(oauthRedirect: String): Completable =
        Completable.defer {
            if (oauthRedirect.contains(hash)) {
                sessionRepository
                    .login(
                        oauthParams.appId,
                        oauthParams.appKey,
                        getQueryParameterFromUri(oauthRedirect, PARAMETER_CODE),
                        oauthParams.redirectUrl
                    )
                    .doOnSuccess { switchAccount(it) }
                    .ignoreElement()
            } else {
                Completable.error(RuntimeException("Not valid oauth hash!"))
            }
        }

    fun login(customServerPath: String, privateToken: String): Completable =
        sessionRepository.login(privateToken, customServerPath)
            .doOnSuccess { switchAccount(it) }
            .ignoreElement()

    fun logout(): Boolean {
        val currentAccount = sessionRepository.getCurrentUserAccount()
        if (currentAccount != null) {
            return logout(currentAccount.userId)
        } else {
            return false
        }
    }

    fun logout(userId: Long): Boolean {
        val currentAccount = sessionRepository.getCurrentUserAccount()
        val newAccount = sessionRepository.logout(userId)
        if (currentAccount != newAccount) {
            projectCache.clear()
            switchAccount(newAccount)
            return true
        } else {
            return false
        }
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

    private fun switchAccount(newAccount: UserAccount?) {
        Toothpick.closeScope(DI.SERVER_SCOPE)
        Toothpick
            .openScopes(DI.APP_SCOPE, DI.SERVER_SCOPE)
            .installModules(ServerModule(newAccount))
    }

    companion object {
        private const val PARAMETER_CODE = "code"
    }
}