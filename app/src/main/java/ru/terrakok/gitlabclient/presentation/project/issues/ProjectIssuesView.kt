package ru.terrakok.gitlabclient.presentation.project.issues

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 27.08.18
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectIssuesView : MvpView {

    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showPageProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showIssues(show: Boolean, issues: List<TargetHeader>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}