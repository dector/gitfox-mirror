package ru.terrakok.gitlabclient.presentation.issue.info

import gitfox.entity.Issue
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

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
