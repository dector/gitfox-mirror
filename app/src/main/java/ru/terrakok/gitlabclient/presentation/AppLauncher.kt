package ru.terrakok.gitlabclient.presentation

import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.interactor.launch.LaunchInteractor
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.09.18.
 */
class AppLauncher @Inject constructor(
    private val launchInteractor: LaunchInteractor,
    private val router: Router
) {

    private val isSignedIn = launchInteractor.signInToSession()

    fun coldStart() {
        val rootScreen =
            if (isSignedIn) Screens.DrawerFlow
            else Screens.AuthFlow

        if (launchInteractor.isFirstLaunch) {
            router.newRootChain(rootScreen, Screens.PrivacyPolicy)
        } else {
            router.newRootScreen(rootScreen)
        }

    }
}