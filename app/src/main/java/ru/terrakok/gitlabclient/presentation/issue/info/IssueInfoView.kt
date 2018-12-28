package ru.terrakok.gitlabclient.presentation.issue.info

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.issue.Issue

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 27.04.17.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface IssueInfoView : MvpView {
    fun showInfo(issue: Issue)
    fun showEmptyProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}