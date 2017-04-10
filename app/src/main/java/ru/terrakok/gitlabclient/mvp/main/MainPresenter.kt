package ru.terrakok.gitlabclient.mvp.main

import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.Screens
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 02.04.17
 */
class MainPresenter : MvpPresenter<MainView>() {
    @Inject lateinit var router: Router

    init {
        App.DAGGER.appComponent.inject(this)
    }

    fun onMenuPressed() = router.navigateTo(Screens.NAVIGATION_DRAWER)
    fun onBackPressed() = router.exit()
}