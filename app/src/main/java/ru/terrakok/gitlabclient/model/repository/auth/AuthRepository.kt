package ru.terrakok.gitlabclient.model.repository.auth

import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultServerPath
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
class AuthRepository @Inject constructor(
        private val authData: AuthHolder,
        private val api: GitlabApi,
        private val schedulers: SchedulersProvider,
        @DefaultServerPath private val defaultServerPath: String
) {

    val isSignedIn get() = !authData.token.isNullOrEmpty()

    fun requestOAuthToken(
            appId: String,
            appKey: String,
            code: String,
            redirectUri: String
    ) = api
            .auth(appId, appKey, code, redirectUri)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun saveAuthData(
            token: String,
            serverPath: String,
            isOAuthToken: Boolean
    ) {
        authData.token = token
        authData.serverPath = serverPath
        authData.isOAuthToken = isOAuthToken
    }

    fun clearAuthData() {
        authData.token = null
        authData.serverPath = defaultServerPath
    }
}