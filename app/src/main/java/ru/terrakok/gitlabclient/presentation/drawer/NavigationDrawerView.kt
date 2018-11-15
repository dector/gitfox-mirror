package ru.terrakok.gitlabclient.presentation.drawer

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.app.session.UserAccount

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

    fun showUserAccount(userAccount: UserAccount)
    fun selectMenuItem(item: MenuItem)
}