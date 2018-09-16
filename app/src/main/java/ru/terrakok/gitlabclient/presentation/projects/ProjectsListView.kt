package ru.terrakok.gitlabclient.presentation.projects

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.Project

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.03.17
 */

@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectsListView : MvpView {
    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showPageProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showProjects(show: Boolean, projects: List<Project>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}