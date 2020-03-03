package ru.terrakok.gitlabclient.presentation.project

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 01.11.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectView : MvpView {
    fun setTitle(title: String, shareUrl: String?)
    fun showBlockingProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
