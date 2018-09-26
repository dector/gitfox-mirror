package ru.terrakok.gitlabclient.presentation

import com.arellomobile.mvp.MvpPresenter
import com.arellomobile.mvp.MvpView
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.model.interactor.launch.LaunchInteractor
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.09.18.
 */
class AppPresenter @Inject constructor(
    private val router: Router,
    private val authInteractor: AuthInteractor,
    private val launchInteractor: LaunchInteractor
) : MvpPresenter<MvpView>() {

    fun coldStart() {
        if (authInteractor.isSignedIn()) {
            router.newRootScreen(Screens.DRAWER_FLOW)
        } else {
            router.newRootScreen(Screens.AUTH_FLOW)
        }

        if (launchInteractor.isFirstLaunch) {
            router.navigateTo(Screens.PRIVACY_POLICY_FLOW)
        }
    }
}