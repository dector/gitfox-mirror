package ru.terrakok.gitlabclient.model.interactor

import ru.terrakok.gitlabclient.model.data.state.SessionSwitcher
import ru.terrakok.gitlabclient.model.data.storage.Prefs

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 26.09.18.
 */
class LaunchInteractor(
    private val prefs: Prefs,
    private val sessionSwitcher: SessionSwitcher
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
        if (!sessionSwitcher.hasSession()) {
            sessionSwitcher.initSession(prefs.getCurrentUserAccount())
        }
    }
}
