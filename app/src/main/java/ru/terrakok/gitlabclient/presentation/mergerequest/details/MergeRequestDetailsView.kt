package ru.terrakok.gitlabclient.presentation.mergerequest.details

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 31.05.19.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MergeRequestDetailsView : MvpView {

    fun showDetails(mr: MergeRequest, mdDescription: CharSequence)
    fun showEmptyProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}