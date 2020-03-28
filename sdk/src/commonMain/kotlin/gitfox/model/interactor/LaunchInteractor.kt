package gitfox.model.interactor

import gitfox.model.data.state.SessionSwitcher
import gitfox.model.data.storage.Prefs
import gitfox.util.currentTimeMillis

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 26.09.18.
 */
class LaunchInteractor internal constructor(
    private val prefs: Prefs,
    private val sessionSwitcher: SessionSwitcher
) {

    val isFirstLaunch: Boolean
        get() {
            val timeStamp = prefs.firstLaunchTimeStamp
            if (timeStamp == null) {
                prefs.firstLaunchTimeStamp = currentTimeMillis()
                return true
            } else {
                return false
            }
        }

    val hasAccount: Boolean
        get() = prefs.selectedAccount != null

    fun signInToLastSession() {
        if (!sessionSwitcher.hasSession) {
            sessionSwitcher.initSession(prefs.getCurrentUserAccount())
        }
    }
}
