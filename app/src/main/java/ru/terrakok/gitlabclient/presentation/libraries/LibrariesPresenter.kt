package ru.terrakok.gitlabclient.presentation.libraries

import com.arellomobile.mvp.InjectViewState
import javax.inject.Inject
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.model.interactor.app.AppInfoInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import timber.log.Timber

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