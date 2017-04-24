package ru.terrakok.gitlabclient.mvp.launch

import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.addTo
import ru.terrakok.gitlabclient.model.auth.AuthManager
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
class LaunchPresenter : MvpPresenter<LaunchView>() {
    @Inject lateinit var router: Router
    @Inject lateinit var authManager: AuthManager
    private val compositeDisposable = CompositeDisposable()

    init {
        App.DAGGER.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        authManager.getSignState()
                .firstOrError()
                .subscribe({ isSignedIn ->
                    if (isSignedIn) router.newRootScreen(Screens.MAIN_SCREEN)
                    else router.newRootScreen(Screens.AUTH_SCREEN)
                })
                .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    fun onBackPressed() = router.finishChain()
}