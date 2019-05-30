package ru.terrakok.gitlabclient.presentation.main

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.05.19.
 */
@StateStrategyType(OneExecutionStateStrategy::class)
interface MainView : MvpView {
    fun setAssignedNotifications(issueCount: Int, mergeRequestCount: Int, todoCount: Int)
}