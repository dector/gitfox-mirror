package ru.terrakok.gitlabclient.model.profile

import com.jakewharton.rxrelay2.PublishRelay
import dagger.Lazy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.model.server.ServerManager
import ru.terrakok.gitlabclient.model.storage.Prefs
import timber.log.Timber

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 10.04.17
 */
class ProfileManager(private val prefs: Prefs,
                     private val serverManager: Lazy<ServerManager>,
                     private val router: Router) {
    private val profileCache = PublishRelay.create<User>()

    fun isSignedIn() = !getToken().isNullOrEmpty()
    fun getToken() = prefs.getAuthToken()
    fun updateToken(token: String) = prefs.putAuthToken(token)

    fun logout() {
        prefs.putAuthToken(null)
        router.newRootScreen(Screens.AUTH_SCREEN)
    }

    fun getProfile(): Observable<User> = profileCache
    fun getDomen() = serverManager.get().domen

    fun refreshProfile() {
        if (isSignedIn()) {
            serverManager.get().api.getMyUser()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { profileCache.accept(it) },
                            { Timber.e("getMyUser error: $it") })
        }
    }
}