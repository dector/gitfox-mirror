package ru.terrakok.gitlabclient.presentation.project.milestones

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.presentation.global.Paginator

/**
 * @author Valentin Logvinovitch (glvvl) on 24.11.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectMilestonesView : MvpView {
    fun renderPaginatorState(state: Paginator.State<Milestone>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}