package ru.terrakok.gitlabclient.model.interactor

import javax.inject.Inject
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.di.module.ServerModule
import ru.terrakok.gitlabclient.model.data.storage.Prefs
import timber.log.Timber
import toothpick.Toothpick

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 26.09.18.
 */
class LaunchInteractor @Inject constructor(
    private val prefs: Prefs
) {

    val isFirstLaunch: Boolean
        get() {
            val timeStamp = prefs.firstLaunchTimeStamp
            if (timeStamp == null) {
                prefs.firstLaunchTimeStamp = System.currentTimeMillis()
                return true
            } else {
                return false
            }
        }

    val hasAccount: Boolean
        get() = prefs.selectedAccount != null

    fun signInToLastSession() {
        if (!Toothpick.isScopeOpen(DI.SERVER_SCOPE)) {
            Timber.d("Init new scope: ${DI.SERVER_SCOPE} -> ${DI.APP_SCOPE}")
            val account = prefs.getCurrentUserAccount()
            Toothpick
                .openScopes(DI.APP_SCOPE, DI.SERVER_SCOPE)
                .installModules(ServerModule(account))
        }
    }
}
