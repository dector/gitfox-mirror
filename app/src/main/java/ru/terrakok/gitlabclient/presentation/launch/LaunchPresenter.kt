package ru.terrakok.gitlabclient.presentation.launch

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
@InjectViewState
class LaunchPresenter @Inject constructor(
        private val router: Router,
        private val authInteractor: AuthInteractor
) : BasePresenter<LaunchView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        if (authInteractor.isSignedIn()) viewState.initMainScreen()
        else router.newRootScreen(Screens.AUTH_SCREEN)
    }

    fun onBackPressed() = router.finishChain()
}