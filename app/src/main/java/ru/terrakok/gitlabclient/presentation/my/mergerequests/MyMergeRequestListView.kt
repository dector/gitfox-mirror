package ru.terrakok.gitlabclient.presentation.my.mergerequests

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader

@StateStrategyType(AddToEndSingleStrategy::class)
interface MyMergeRequestListView : MvpView {
    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showPageProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showMergeRequests(show: Boolean, mergeRequests: List<TargetHeader>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}