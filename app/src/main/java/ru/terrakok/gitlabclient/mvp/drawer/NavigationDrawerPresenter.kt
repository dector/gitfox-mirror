package ru.terrakok.gitlabclient.mvp.drawer

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.mvp.drawer.NavigationDrawerView.MenuItem
import ru.terrakok.gitlabclient.mvp.drawer.NavigationDrawerView.MenuItem.PROJECTS
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 04.04.17
 */
@InjectViewState
class NavigationDrawerPresenter : MvpPresenter<NavigationDrawerView>() {
    @Inject lateinit var router: Router

    private var currentSelectedItem: MenuItem? = null

    init {
        App.DAGGER.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        viewState.showVersionName("${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
    }

    fun onScreenChanged(item: MenuItem) {
        currentSelectedItem = item
        viewState.selectMenuItem(item)
    }

    fun onMenuItemClick(item: MenuItem) {
        if (item != currentSelectedItem) {
            when (item) {
                PROJECTS -> router.newRootScreen(Screens.MAIN_SCREEN)
                else -> router.showSystemMessage("Unknown screen yet!") //todo
            }
        }
    }
}