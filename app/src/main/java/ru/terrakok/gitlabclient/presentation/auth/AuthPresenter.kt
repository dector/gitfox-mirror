package ru.terrakok.gitlabclient.presentation.auth

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.interactor.SessionInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
@InjectViewState
class AuthPresenter @Inject constructor(
    private val router: FlowRouter,
    private val sessionInteractor: SessionInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<AuthView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        startAuthorization()
    }

    private fun startAuthorization() {
        viewState.loadUrl(sessionInteractor.oauthUrl)
    }

    private fun requestToken(url: String) {
        sessionInteractor.login(url)
            .doOnSubscribe { viewState.showProgress(true) }
            .doAfterTerminate { viewState.showProgress(false) }
            .subscribe(
                { router.newRootFlow(Screens.DrawerFlow) },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            ).connect()
    }

    fun onRedirect(url: String): Boolean {
        if (sessionInteractor.checkOAuthRedirect(url)) {
            requestToken(url)
            return true
        } else {
            viewState.loadUrl(url)
            return false
        }
    }

    fun refresh() {
        startAuthorization()
    }

    fun loginOnCustomServer(url: String, token: String) {
        sessionInteractor.loginOnCustomServer(token, url)
            .subscribe(
                { router.newRootFlow(Screens.DrawerFlow) },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            ).connect()
    }

    fun onBackPressed() = router.exit()
}