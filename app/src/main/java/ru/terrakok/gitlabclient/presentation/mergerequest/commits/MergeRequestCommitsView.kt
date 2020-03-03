package ru.terrakok.gitlabclient.presentation.mergerequest.commits

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.presentation.global.Paginator

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.10.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MergeRequestCommitsView : MvpView {
    fun renderPaginatorState(state: Paginator.State)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
