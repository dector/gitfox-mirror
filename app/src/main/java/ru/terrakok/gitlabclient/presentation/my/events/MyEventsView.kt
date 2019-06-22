package ru.terrakok.gitlabclient.presentation.my.events

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.presentation.global.Paginator

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MyEventsView : MvpView {
    fun renderPaginatorState(state: Paginator.State<TargetHeader>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}