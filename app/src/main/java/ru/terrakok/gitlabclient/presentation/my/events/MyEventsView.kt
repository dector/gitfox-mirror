package ru.terrakok.gitlabclient.presentation.my.events

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MyEventsView : MvpView {
    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showPageProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showEvents(show: Boolean, events: List<TargetHeader>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}