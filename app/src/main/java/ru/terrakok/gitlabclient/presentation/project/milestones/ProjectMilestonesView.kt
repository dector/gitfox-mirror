package ru.terrakok.gitlabclient.presentation.project.milestones

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.presentation.global.Paginator

/**
 * @author Valentin Logvinovitch (glvvl) on 24.11.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectMilestonesView : MvpView {
    fun renderPaginatorState(state: Paginator.State)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}