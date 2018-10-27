package ru.terrakok.gitlabclient.presentation.mergerequest

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 27.10.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MergeRequestView : MvpView {
    fun setTitle(title: String, subtitle: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showBlockingProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}