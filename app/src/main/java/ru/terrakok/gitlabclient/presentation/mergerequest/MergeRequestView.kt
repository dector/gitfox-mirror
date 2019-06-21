package ru.terrakok.gitlabclient.presentation.mergerequest

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
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