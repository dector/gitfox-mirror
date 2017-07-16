package ru.terrakok.gitlabclient.presentation.my.issues

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.common.Issue

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MyIssuesView : MvpView {
    fun showProgress(show: Boolean, fullscreen: Boolean)
    fun showPageProgress(show: Boolean)
    fun showIssues(issues: List<Issue>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}