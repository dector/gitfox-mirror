package ru.terrakok.gitlabclient.mvp.drawer

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.User

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 04.04.17
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface NavigationDrawerView : MvpView {
    enum class MenuItem {
        PROJECTS,
        ACTIVITY,
        GROUPS,
        SETTINGS,
        ABOUT
    }

    fun showVersionName(version: String)
    fun showUserInfo(user: User, serverUrl: String)
    fun selectMenuItem(item: MenuItem)
}