package ru.terrakok.gitlabclient.presentation.libraries

import com.github.aakira.napier.Napier
import gitfox.model.interactor.AppInfoInteractor
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
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
                Napier.e("getAppLibraries error: $e")
            }
        }
    }

    fun onBackPressed() = router.exit()
}
