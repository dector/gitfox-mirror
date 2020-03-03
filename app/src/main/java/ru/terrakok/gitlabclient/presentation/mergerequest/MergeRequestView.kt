package ru.terrakok.gitlabclient.presentation.mergerequest

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.app.target.TargetAction

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 27.10.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MergeRequestView : MvpView {
    fun setTitle(title: String, subtitle: String)
    fun showBlockingProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun selectActionTab(targetAction: TargetAction)
}
