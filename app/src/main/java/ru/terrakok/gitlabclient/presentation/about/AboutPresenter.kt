package ru.terrakok.gitlabclient.presentation.about

import com.github.aakira.napier.Napier
import gitfox.model.interactor.AppInfoInteractor
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.AppDevelopersPath
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import ru.terrakok.gitlabclient.util.e
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.05.17.
 */
@InjectViewState
class AboutPresenter @Inject constructor(
    private val router: FlowRouter,
    private val menuController: GlobalMenuController,
    private val appInfoInteractor: AppInfoInteractor,
    @AppDevelopersPath private val appDevelopersUrl: String
) : BasePresenter<AboutView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        launch {
            try {
                viewState.showAppInfo(appInfoInteractor.getAppInfo())
            } catch (e: Exception) {
                Napier.e(e)
            }
        }
    }

    fun onShowLibrariesClicked() = router.startFlow(Screens.Libraries)

    fun onPrivacyPolicyClicked() = router.startFlow(Screens.PrivacyPolicy)

    fun onDevelopersClicked() = router.startFlow(Screens.ExternalBrowserFlow(appDevelopersUrl))

    fun onMenuPressed() = menuController.open()
    fun onBackPressed() = router.exit()
}
