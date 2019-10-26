package ru.terrakok.gitlabclient.presentation.project.labels

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.presentation.global.Paginator

/**
 * @author Maxim Myalkin (MaxMyalkin) on 11.11.2018.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectLabelsView : MvpView {
    fun renderPaginatorState(state: Paginator.State)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}