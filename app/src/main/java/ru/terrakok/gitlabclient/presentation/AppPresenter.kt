package ru.terrakok.gitlabclient.presentation

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.model.interactor.launch.LaunchInteractor
import ru.terrakok.gitlabclient.model.system.flow.AppRouter
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.09.18.
 */
class AppPresenter @Inject constructor(
    private val router: AppRouter,
    private val authInteractor: AuthInteractor,
    private val launchInteractor: LaunchInteractor
) : MvpPresenter<MvpView>() {

    fun coldStart() {
        val rootScreen =
            if (authInteractor.isSignedIn()) Screens.DRAWER_FLOW
            else Screens.AUTH_FLOW

        if (launchInteractor.isFirstLaunch) {
            router.newRootScreens(
                Pair(rootScreen, null),
                Pair(Screens.PRIVACY_POLICY_FLOW, null)
            )
        } else {
            router.newRootScreen(rootScreen)
        }

    }
}