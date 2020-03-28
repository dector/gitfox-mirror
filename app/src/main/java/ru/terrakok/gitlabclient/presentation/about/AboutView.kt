package ru.terrakok.gitlabclient.presentation.about

import gitfox.entity.app.develop.AppInfo
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.05.17.
 */

@StateStrategyType(AddToEndSingleStrategy::class)
interface AboutView : MvpView {
    fun showAppInfo(appInfo: AppInfo)
}
