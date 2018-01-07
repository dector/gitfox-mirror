package ru.terrakok.gitlabclient.presentation.main

import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 02.04.17
 */
class MainPresenter @Inject constructor(
        private val router: Router
) : BasePresenter<MainView>() {

    fun onBackPressed() = router.exit()
}