package ru.terrakok.gitlabclient.presentation.drawer

import gitfox.entity.app.session.UserAccount
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 04.04.17
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface NavigationDrawerView : MvpView {
    enum class MenuItem {
        ACTIVITY,
        PROJECTS,
        ABOUT
    }

    fun selectMenuItem(item: MenuItem)
    fun setAccounts(accounts: List<UserAccount>, currentAccount: UserAccount)
}
