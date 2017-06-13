package ru.terrakok.gitlabclient.presentation.main

import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.App
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 02.04.17
 */
class MainPresenter : MvpPresenter<MainView>() {
    @Inject lateinit var router: Router

    init {
        App.DAGGER.appComponent.inject(this)
    }

    fun onBackPressed() = router.exit()
}