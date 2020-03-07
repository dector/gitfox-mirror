package ru.terrakok.gitlabclient.presentation.issue.details

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.Issue

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 26.05.19.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface IssueDetailsView : MvpView {

    fun showDetails(issue: Issue, mdDescription: CharSequence)
    fun showEmptyProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
