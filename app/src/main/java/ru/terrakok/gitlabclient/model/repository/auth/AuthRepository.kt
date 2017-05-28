package ru.terrakok.gitlabclient.model.repository.auth

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
class AuthRepository(private val authData: AuthHolder,
                     private val api: GitlabApi,
                     private val schedulers: SchedulersProvider) {

    private val signState = BehaviorRelay.createDefault(!authData.getAuthToken().isNullOrEmpty())

    fun getSignState(): Observable<Boolean> = signState

    fun refreshServerToken(appId: String, appKey: String, code: String, redirectUri: String) =
            api.auth(appId, appKey, code, redirectUri)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
                    .doOnSuccess {
                        authData.putAuthToken(it.token)
                        signState.accept(!it.token.isNullOrEmpty())
                    }
                    .toCompletable()

    fun clearToken() = Completable.defer {
        authData.putAuthToken(null)
        signState.accept(false)
        Completable.complete()
    }
}