package ru.terrakok.gitlabclient.presentation.main

import gitfox.entity.app.AccountMainBadges
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.05.19.
 */
@StateStrategyType(OneExecutionStateStrategy::class)
interface MainView : MvpView {
    fun setAssignedNotifications(badges: AccountMainBadges)
}
