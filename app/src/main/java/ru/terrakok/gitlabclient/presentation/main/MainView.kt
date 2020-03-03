package ru.terrakok.gitlabclient.presentation.main

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.app.AccountMainBadges

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.05.19.
 */
@StateStrategyType(OneExecutionStateStrategy::class)
interface MainView : MvpView {
    fun setAssignedNotifications(badges: AccountMainBadges)
}
