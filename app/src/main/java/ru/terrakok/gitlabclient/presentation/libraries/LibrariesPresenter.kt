package ru.terrakok.gitlabclient.presentation.libraries

import moxy.InjectViewState
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.model.interactor.AppInfoInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 23.12.17.
 */
@InjectViewState
class LibrariesPresenter @Inject constructor(
    private val router: Router,
    private val appInfoInteractor: AppInfoInteractor
) : BasePresenter<LibrariesView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        appInfoInteractor
            .getAppLibraries()
            .subscribe(
                { viewState.showLibraries(it) },
                { Timber.e("getAppLibraries error: $it") }
            )
            .connect()
    }

    fun onBackPressed() = router.exit()
}