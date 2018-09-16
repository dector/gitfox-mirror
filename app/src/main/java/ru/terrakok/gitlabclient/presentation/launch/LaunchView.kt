package ru.terrakok.gitlabclient.presentation.launch

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

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