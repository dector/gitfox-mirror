package ru.terrakok.gitlabclient.presentation.mergerequest.changes

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.DiffData

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 26.10.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MergeRequestDiffDataListView : MvpView {

    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showDiffDataList(show: Boolean, diffDataList: List<DiffData>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)

    fun showFullscreenProgress(show: Boolean)
}
