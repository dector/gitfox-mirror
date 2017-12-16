package ru.terrakok.gitlabclient.presentation.about

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.addTo
import ru.terrakok.gitlabclient.model.interactor.app.AppInfoInteractor
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import timber.log.Timber
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.05.17.
 */
@InjectViewState
class AboutPresenter @Inject constructor(
        private val router: Router,
        private val menuController: GlobalMenuController,
        private val appInfoInteractor: AppInfoInteractor
) : MvpPresenter<AboutView>() {

    private val compositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        appInfoInteractor
                .getAppInfo()
                .subscribe(
                        { viewState.showAppInfo(it) },
                        { Timber.e(it) }
                )
                .addTo(compositeDisposable)
        appInfoInteractor
                .getAppDevelopers()
                .subscribe(
                        { viewState.showAppDevelopers(it) },
                        { Timber.e(it) }
                )
                .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun onShowLibrariesClicked() {}

    fun onDeveloperClicked(id: Long) = router.navigateTo(Screens.USER_INFO_SCREEN, id)

    fun onMenuPressed() = menuController.open()
    fun onBackPressed() = router.exit()
}