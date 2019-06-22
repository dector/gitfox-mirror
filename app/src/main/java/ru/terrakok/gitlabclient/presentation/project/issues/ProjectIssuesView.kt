package ru.terrakok.gitlabclient.presentation.project.issues

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.presentation.global.Paginator

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 27.08.18
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectIssuesView : MvpView {
    fun renderPaginatorState(state: Paginator.State<TargetHeader>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}