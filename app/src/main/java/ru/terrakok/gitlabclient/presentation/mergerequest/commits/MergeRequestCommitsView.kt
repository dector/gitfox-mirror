package ru.terrakok.gitlabclient.presentation.mergerequest.commits

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
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