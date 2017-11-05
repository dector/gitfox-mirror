package ru.terrakok.gitlabclient.presentation.auth

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.addTo
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
@InjectViewState
class AuthPresenter @Inject constructor(
        private val router: Router,
        private val authInteractor: AuthInteractor,
        private val resourceManager: ResourceManager,
        private val errorHandler: ErrorHandler
) : MvpPresenter<AuthView>() {

    private var compositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        startAuthorization()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    private fun startAuthorization() {
        viewState.loadUrl(authInteractor.oauthUrl)
    }

    private fun requestToken(url: String) {
        authInteractor.login(url)
                .doOnSubscribe { viewState.showProgress(true) }
                .doAfterTerminate { viewState.showProgress(false) }
                .subscribe(
                        { router.newRootScreen(Screens.MAIN_SCREEN) },
                        { errorHandler.proceed(it, { viewState.showMessage(it) }) }
                ).addTo(compositeDisposable)
    }

    fun onRedirect(url: String): Boolean {
        if (authInteractor.checkOAuthRedirect(url)) {
            requestToken(url)
            return true
        } else {
            viewState.loadUrl(url)
            return false
        }
    }

    fun loginOnCustomServer(url: String, token: String) {
        authInteractor.login(url, token)
                .subscribe(
                        { router.newRootScreen(Screens.MAIN_SCREEN) },
                        { errorHandler.proceed(it, { viewState.showMessage(it) }) }
                ).addTo(compositeDisposable)
    }

    fun onBackPressed() = router.exit()
}