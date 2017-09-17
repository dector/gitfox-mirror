package ru.terrakok.gitlabclient.presentation.auth

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.addTo
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.model.system.ServerSwitcher
import timber.log.Timber
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
@InjectViewState
class AuthPresenter @Inject constructor(
        private val router: Router,
        private val authInteractor: AuthInteractor,
        private val serverSwitcher: ServerSwitcher,
        private val resourceManager: ResourceManager
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
                        {
                            serverSwitcher.switchOnNewServer()
                        },
                        { error ->
                            Timber.e("Auth error: $error")
                            viewState.showMessage(resourceManager.getString(R.string.auth_error))
                        }
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
                        {
                            serverSwitcher.switchOnNewServer()
                        },
                        { error ->
                            Timber.e("Auth error: $error")
                            viewState.showMessage(resourceManager.getString(R.string.auth_error))
                        }
                ).addTo(compositeDisposable)
    }

    fun onBackPressed() = router.exit()
}