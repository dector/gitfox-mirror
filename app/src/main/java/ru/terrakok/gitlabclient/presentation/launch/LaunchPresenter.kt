package ru.terrakok.gitlabclient.presentation.launch

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
@InjectViewState
class LaunchPresenter @Inject constructor(
        private val router: Router,
        private val authInteractor: AuthInteractor
) : MvpPresenter<LaunchView>() {

    override fun onFirstViewAttach() {
        if (authInteractor.isSignedIn()) viewState.initMainScreen()
        else router.newRootScreen(Screens.AUTH_SCREEN)
    }

    fun onBackPressed() = router.finishChain()
}