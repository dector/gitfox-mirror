package ru.terrakok.gitlabclient.presentation.project.labels

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.Label

/**
 * @author Maxim Myalkin (MaxMyalkin) on 11.11.2018.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectLabelsView : MvpView {

    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showPageProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showLabels(show: Boolean, list: List<Label>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)

}