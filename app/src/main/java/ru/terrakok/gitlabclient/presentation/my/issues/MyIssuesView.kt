package ru.terrakok.gitlabclient.presentation.my.issues

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.presentation.global.Paginator

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MyIssuesView : MvpView {
    fun renderPaginatorState(state: Paginator.State)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
