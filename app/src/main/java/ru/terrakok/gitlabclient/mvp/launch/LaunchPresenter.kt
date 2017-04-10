package ru.terrakok.gitlabclient.mvp.launch

import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.profile.ProfileManager
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
class LaunchPresenter : MvpPresenter<LaunchView>() {
    @Inject lateinit var router: Router
    @Inject lateinit var profileManager: ProfileManager

    init {
        App.DAGGER.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        if (!profileManager.isSignedIn()) {
            router.newRootScreen(Screens.AUTH_SCREEN)
        } else {
            profileManager.refreshProfile()
            router.newRootScreen(Screens.MAIN_SCREEN)
        }
    }

    fun onBackPressed() = router.finishChain()
}