package ru.terrakok.gitlabclient.presentation.drawer

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.model.profile.MyUserInfo

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
    fun showUserInfo(user: MyUserInfo)
    fun selectMenuItem(item: MenuItem)
}