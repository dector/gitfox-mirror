package ru.terrakok.gitlabclient.presentation.main

import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 02.04.17
 */
class MainPresenter @Inject constructor(
        private val router: Router
) : MvpPresenter<MainView>() {

    fun onBackPressed() = router.exit()
}