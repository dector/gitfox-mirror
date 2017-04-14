package ru.terrakok.gitlabclient.mvp.auth

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

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