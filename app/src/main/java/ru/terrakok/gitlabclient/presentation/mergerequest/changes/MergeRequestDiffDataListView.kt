package ru.terrakok.gitlabclient.presentation.mergerequest.changes

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
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