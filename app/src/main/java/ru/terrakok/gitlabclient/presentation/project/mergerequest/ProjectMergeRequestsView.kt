package ru.terrakok.gitlabclient.presentation.project.mergerequest

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.presentation.global.Paginator

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 28.08.18
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectMergeRequestsView : MvpView {
    fun renderPaginatorState(state: Paginator.State)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}