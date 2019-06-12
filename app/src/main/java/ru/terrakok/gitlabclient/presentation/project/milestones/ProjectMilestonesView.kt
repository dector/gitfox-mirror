package ru.terrakok.gitlabclient.presentation.project.milestones

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.milestone.Milestone

/**
 * @author Valentin Logvinovitch (glvvl) on 24.11.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectMilestonesView : MvpView {

    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showPageProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showMilestones(show: Boolean, milestones: List<Milestone>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}