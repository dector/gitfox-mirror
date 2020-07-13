package ru.terrakok.gitlabclient.presentation.commit

import gitfox.entity.Commit
import gitfox.entity.DiffData
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * @author Valentin Logvinovitch (glvvl) on 18.06.19.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface CommitView : MvpView {

    fun showBlockingProgress(show: Boolean)
    fun showCommitInfo(commit: Commit)

    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showDiffDataList(show: Boolean, diffDataList: List<DiffData>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
