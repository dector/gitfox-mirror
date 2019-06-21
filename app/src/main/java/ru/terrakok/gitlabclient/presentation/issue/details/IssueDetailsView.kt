package ru.terrakok.gitlabclient.presentation.issue.details

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.issue.Issue

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 26.05.19.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface IssueDetailsView : MvpView {

    fun showDetails(issue: Issue)
    fun showEmptyProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}