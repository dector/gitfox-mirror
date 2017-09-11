package ru.terrakok.gitlabclient.presentation.about

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.05.17.
 */
@InjectViewState
class AboutPresenter @Inject constructor(
        private val router: Router,
        private val menuController: GlobalMenuController
) : MvpPresenter<AboutView>() {

    override fun onFirstViewAttach() {
        viewState.showAppVersion("${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
    }

    fun onMenuPressed() = menuController.open()
    fun onBackPressed() = router.exit()
}