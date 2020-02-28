package ru.terrakok.gitlabclient.presentation.my.todos

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.presentation.global.Paginator

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 27.09.17
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MyTodoListView : MvpView {
    fun renderPaginatorState(state: Paginator.State)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
