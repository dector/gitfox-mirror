package ru.terrakok.gitlabclient.presentation.commit

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.Commit
import ru.terrakok.gitlabclient.entity.DiffData

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