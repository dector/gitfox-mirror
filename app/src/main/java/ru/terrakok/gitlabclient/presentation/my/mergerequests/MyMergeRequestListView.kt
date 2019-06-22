package ru.terrakok.gitlabclient.presentation.my.mergerequests

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.presentation.global.Paginator

@StateStrategyType(AddToEndSingleStrategy::class)
interface MyMergeRequestListView : MvpView {
    fun renderPaginatorState(state: Paginator.State<TargetHeader>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}