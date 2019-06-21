package ru.terrakok.gitlabclient.presentation.mergerequest.commits

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.app.CommitWithShortUser

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.10.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MergeRequestCommitsView : MvpView {

    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showPageProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showCommits(show: Boolean, commits: List<CommitWithShortUser>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}