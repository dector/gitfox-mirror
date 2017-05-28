package ru.terrakok.gitlabclient.presentation.auth

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.addTo
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.model.system.ResourceManager
import timber.log.Timber
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
@InjectViewState
class AuthPresenter : MvpPresenter<AuthView>() {
    @Inject lateinit var router: Router
    @Inject lateinit var authInteractor: AuthInteractor
    @Inject lateinit var resourceManager: ResourceManager

    private var compositeDisposable = CompositeDisposable()

    init {
        App.DAGGER.appComponent.inject(this)
    }

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
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { viewState.showProgress(true) }
                .doOnEvent { viewState.showProgress(false) }
                .subscribe(
                        {
                            router.replaceScreen(Screens.MAIN_SCREEN)
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

    fun onBackPressed() = router.exit()
}