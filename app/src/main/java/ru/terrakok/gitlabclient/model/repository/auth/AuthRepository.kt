package ru.terrakok.gitlabclient.model.repository.auth

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.GitlabApi

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
class AuthRepository(private val authData: AuthHolder, private val api: GitlabApi) {

    private val signState = BehaviorRelay.createDefault(!authData.getAuthToken().isNullOrEmpty())

    fun getSignState(): Observable<Boolean> = signState

    fun refreshServerToken(appId: String, appKey: String, code: String, redirectUri: String) =
            api.auth(appId, appKey, code, redirectUri)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
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