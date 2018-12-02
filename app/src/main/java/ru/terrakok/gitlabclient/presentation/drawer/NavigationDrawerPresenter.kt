package ru.terrakok.gitlabclient.presentation.drawer

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.model.interactor.session.SessionInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView.MenuItem
import ru.terrakok.gitlabclient.presentation.drawer.NavigationDrawerView.MenuItem.*
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 04.04.17
 */
@InjectViewState
class NavigationDrawerPresenter @Inject constructor(
    private val router: FlowRouter,
    private val menuController: GlobalMenuController,
    private val sessionInteractor: SessionInteractor
) : BasePresenter<NavigationDrawerView>() {

    private var currentSelectedItem: MenuItem? = null
    private var userAccount: UserAccount? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        sessionInteractor.getCurrentUserAccount()?.let { acc ->
            this.userAccount = acc
            viewState.setAccounts(sessionInteractor.getUserAccounts(), acc)
        }
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
                ACTIVITY -> router.newRootScreen(Screens.MainFlow)
                PROJECTS -> router.newRootScreen(Screens.ProjectsContainer)
                ABOUT -> router.newRootScreen(Screens.About)
            }
        }
    }

    fun onLogoutClick() {
        menuController.close()
        userAccount?.let {
            val hasOtherAccount = sessionInteractor.logout(it.id)
            if (hasOtherAccount) {
                router.newRootFlow(Screens.DrawerFlow)
            } else {
                router.newRootFlow(Screens.AuthFlow)
            }
        }
    }

    fun onUserClick() {
        menuController.close()
        userAccount?.let {
            router.startFlow(Screens.UserFlow(it.userId))
        }
    }

    fun onAccountClick(account: UserAccount) {
        if (account != userAccount) {
            sessionInteractor.setCurrentUserAccount(account.id)?.let { acc ->
                router.newRootFlow(Screens.DrawerFlow)
            }
        }
    }

    fun onAddAccountClick() {
        router.startFlow(Screens.AuthFlow)
    }
}