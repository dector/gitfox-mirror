package ru.terrakok.gitlabclient.presentation.auth

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface AuthView : MvpView {
    fun loadUrl(url: String)
    fun showProgress(isVisible: Boolean)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}
