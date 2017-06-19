package ru.terrakok.gitlabclient.presentation.about

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.Screens
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.05.17.
 */
@InjectViewState
class AboutPresenter : MvpPresenter<AboutView>() {
    @Inject lateinit var router: Router

    override fun onFirstViewAttach() {
        viewState.showAppVersion("${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
    }

    fun onMenuPressed() = router.navigateTo(Screens.NAVIGATION_DRAWER)
    fun onBackPressed() = router.exit()
}