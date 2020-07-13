package ru.terrakok.gitlabclient.presentation.mergerequest.details

import gitfox.entity.MergeRequest
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 31.05.19.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MergeRequestDetailsView : MvpView {

    fun showDetails(mr: MergeRequest)
    fun showEmptyProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
