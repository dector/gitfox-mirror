package ru.terrakok.gitlabclient.presentation.about

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.interactor.app.AppInfoInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import ru.terrakok.gitlabclient.toothpick.qualifier.AppDevelopersPath
import timber.log.Timber
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

        appInfoInteractor
            .getAppInfo()
            .subscribe(
                { viewState.showAppInfo(it) },
                { Timber.e(it) }
            )
            .connect()
    }

    fun onShowLibrariesClicked() = router.startFlow(Screens.Libraries)

    fun onPrivacyPolicyClicked() = router.startFlow(Screens.PrivacyPolicy)

    fun onDevelopersClicked() = viewState.showAppDevelopers(appDevelopersUrl)

    fun onMenuPressed() = menuController.open()
    fun onBackPressed() = router.exit()
}