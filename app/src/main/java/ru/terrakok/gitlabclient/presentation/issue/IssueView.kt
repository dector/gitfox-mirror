package ru.terrakok.gitlabclient.presentation.issue

import gitfox.entity.app.target.TargetAction
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 01.11.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface IssueView : MvpView {
    fun setTitle(title: String, subtitle: String)
    fun showBlockingProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun selectActionTab(targetAction: TargetAction)
}
