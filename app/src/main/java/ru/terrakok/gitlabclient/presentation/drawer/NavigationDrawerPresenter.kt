package ru.terrakok.gitlabclient.presentation.drawer

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.model.interactor.profile.MyProfileInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView.MenuItem
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView.MenuItem.*
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 04.04.17
 */
@InjectViewState
class NavigationDrawerPresenter @Inject constructor(
    private val router: FlowRouter,
    private val menuController: GlobalMenuController,
    private val authInteractor: AuthInteractor,
    private val myProfileInteractor: MyProfileInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<NavigationDrawerView>() {

    private var currentSelectedItem: MenuItem? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        myProfileInteractor.getMyProfile()
            .subscribe(
                { viewState.showUserInfo(it) },
                { errorHandler.proceed(it) }
            )
            .connect()
    }

    fun onScreenChanged(item: MenuItem) {
        menuController.close()
        currentSelectedItem = item
        viewState.selectMenuItem(item)
    }

    fun onMenuItemClick(item: MenuItem) {
        menuController.close()
        if (item != currentSelectedItem) {
            when (item) {
                ACTIVITY -> router.newRootScreen(Screens.MAIN_FLOW)
                PROJECTS -> router.newRootScreen(Screens.PROJECTS_CONTAINER_SCREEN)
                ABOUT -> router.newRootScreen(Screens.ABOUT_SCREEN)
            }
        }
    }

    fun onLogoutClick() {
        menuController.close()
        authInteractor.logout()
        router.newRootFlow(Screens.AUTH_FLOW)
    }

    fun onUserClick(id: Long) {
        menuController.close()
        router.startFlow(Screens.USER_FLOW, id)
    }
}