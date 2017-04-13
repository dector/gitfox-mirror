package ru.terrakok.gitlabclient.mvp.auth

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.addTo
import ru.terrakok.gitlabclient.model.resources.ResourceManager
import ru.terrakok.gitlabclient.model.server.ServerManager
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
@InjectViewState
class AuthPresenter : MvpPresenter<AuthView>() {
    @Inject lateinit var router: Router
    @Inject lateinit var serverManager: ServerManager
    @Inject lateinit var resourceManager: ResourceManager

    private val authHash = UUID.randomUUID().toString()
    private var compositeDisposable = CompositeDisposable()

    init {
        App.DAGGER.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        startAuthorization()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    private fun startAuthorization() {
        viewState.loadUrl(serverManager.getAuthUrl(authHash))
    }

    private fun requestToken(code: String) {
        serverManager.auth(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
        if (serverManager.checkAuthRedirect(url)) {
            if (!url.contains(authHash)) {
                router.exitWithMessage(resourceManager.getString(R.string.invalid_hash))
            } else {
                requestToken(serverManager.getCodeFromAuthRedirect(url))
            }
            return true
        } else {
            viewState.loadUrl(url)
            return false
        }
    }

    fun onBackPressed() = router.exit()

}