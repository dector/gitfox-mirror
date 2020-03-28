package ru.terrakok.gitlabclient.presentation.mergerequest.info

import gitfox.entity.MergeRequest
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 27.04.17.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface MergeRequestInfoView : MvpView {

    fun showInfo(mr: MergeRequest)
    fun showEmptyProgress(show: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
