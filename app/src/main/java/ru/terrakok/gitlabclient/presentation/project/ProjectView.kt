package ru.terrakok.gitlabclient.presentation.project

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

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