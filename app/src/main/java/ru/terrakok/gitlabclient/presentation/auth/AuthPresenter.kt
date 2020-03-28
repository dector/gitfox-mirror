package ru.terrakok.gitlabclient.presentation.auth

import gitfox.model.interactor.SessionInteractor
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.Screens
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

    fun coldStart() {
        startAuthorization()
    }

    private fun startAuthorization() {
        viewState.loadUrl(sessionInteractor.oauthUrl)
    }

    private fun requestToken(url: String) {
        launch {
            viewState.showProgress(true)
            try {
                sessionInteractor.login(url)
                router.newRootFlow(Screens.DrawerFlow)
            } catch (e: Exception) {
                errorHandler.proceed(e) { viewState.showMessage(it) }
            }
            viewState.showProgress(false)
        }
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
        launch {
            try {
                sessionInteractor.loginOnCustomServer(url, token)
                router.newRootFlow(Screens.DrawerFlow)
            } catch (e: Exception) {
                errorHandler.proceed(e) { viewState.showMessage(it) }
            }
        }
    }

    fun onBackPressed() = router.exit()
}
