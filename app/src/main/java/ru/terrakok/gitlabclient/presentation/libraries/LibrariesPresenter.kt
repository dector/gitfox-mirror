package ru.terrakok.gitlabclient.presentation.libraries

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.extension.addTo
import ru.terrakok.gitlabclient.model.interactor.app.AppInfoInteractor
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 23.12.17.
 */
@InjectViewState
class LibrariesPresenter @Inject constructor(
        private val router: Router,
        private val appInfoInteractor: AppInfoInteractor
) : MvpPresenter<LibrariesView>() {

    private val compositeDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        appInfoInteractor
                .getAppLibraries()
                .subscribe(
                        { viewState.showLibraries(it) },
                        { Timber.e("getAppLibraries error: $it") }
                )
                .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    fun onBackPressed() = router.exit()
}