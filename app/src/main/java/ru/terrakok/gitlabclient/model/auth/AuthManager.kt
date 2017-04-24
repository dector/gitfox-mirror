package ru.terrakok.gitlabclient.model.auth

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ru.terrakok.gitlabclient.model.server.ServerData
import java.util.*

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
class AuthManager(
        private val authData: AuthData,
        private val serverData: ServerData,
        private val tokenRepository: TokenRepository) {

    private val hash = UUID.randomUUID().toString()
    private val signState = BehaviorRelay.createDefault(!authData.getAuthToken().isNullOrEmpty())

    val oauthUrl = "${serverData.SERVER_URL}oauth/authorize?client_id=${serverData.APP_ID}" +
            "&redirect_uri=${serverData.AUTH_REDIRECT_URI}&response_type=code&state=$hash"


    fun getSignState(): Observable<Boolean> = signState

    fun checkOAuthRedirect(url: String) = url.indexOf(serverData.AUTH_REDIRECT_URI) == 0

    fun auth(oauthRedirect: String) =
            Completable.defer {
                if (oauthRedirect.contains(hash)) {
                    tokenRepository
                            .getToken(
                                    serverData.APP_ID,
                                    serverData.APP_KEY,
                                    getCodeFromAuthRedirect(oauthRedirect),
                                    serverData.AUTH_REDIRECT_URI)
                            .subscribeOn(Schedulers.io())
                            .doOnSuccess {
                                authData.putAuthToken(it.token)
                                signState.accept(!authData.getAuthToken().isNullOrEmpty())
                            }
                            .toCompletable()
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

    fun logout() = Completable.defer {
        authData.putAuthToken(null)
        signState.accept(false)
        Completable.complete()
    }
}