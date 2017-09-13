package ru.terrakok.gitlabclient.model.repository.auth

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
class AuthRepository @Inject constructor(
        private val authData: AuthHolder,
        private val api: GitlabApi,
        private val schedulers: SchedulersProvider
) {

    private val signState = BehaviorRelay.createDefault(!authData.token.isNullOrEmpty())

    fun getSignState(): Observable<Boolean> = signState

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
        signState.accept(!token.isNullOrEmpty())
    }

    fun clearAuthData() {
        authData.token = null
        authData.serverPath = null
        signState.accept(false)
    }
}