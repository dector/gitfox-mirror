package ru.terrakok.gitlabclient.presentation.issue

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.issue.Issue

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 27.04.17.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface IssueInfoView : MvpView {
    data class IssueInfo(val issue: Issue, val project: Project, val htmlDescription: String)

    fun showIssue(issueInfo: IssueInfo)
    fun showProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}