package ru.terrakok.gitlabclient.presentation.projects

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.presentation.global.Paginator

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.03.17
 */

@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectsListView : MvpView {
    fun renderPaginatorState(state: Paginator.State)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}