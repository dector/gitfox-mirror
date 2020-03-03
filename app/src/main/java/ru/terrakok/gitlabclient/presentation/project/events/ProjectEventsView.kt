package ru.terrakok.gitlabclient.presentation.project.events

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.presentation.global.Paginator

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectEventsView : MvpView {
    fun renderPaginatorState(state: Paginator.State)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
