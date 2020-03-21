package ru.terrakok.gitlabclient.presentation.libraries

import kotlinx.coroutines.launch
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
        launch {
            try {
                viewState.showLibraries(appInfoInteractor.getAppLibraries())
            } catch (e: Exception) {
                Timber.e("getAppLibraries error: $e")
            }
        }
    }

    fun onBackPressed() = router.exit()
}
