package ru.terrakok.gitlabclient.presentation.launch

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
@StateStrategyType(OneExecutionStateStrategy::class)
interface LaunchView : MvpView {

    /**
     * This method display main screen and nav drawer
     * only after authorization checking.
     */
    fun initMainScreen()
}