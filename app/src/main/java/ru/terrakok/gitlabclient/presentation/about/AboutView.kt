package ru.terrakok.gitlabclient.presentation.about

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.app.develop.AppDeveloper
import ru.terrakok.gitlabclient.entity.app.develop.AppInfo

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.05.17.
 */

@StateStrategyType(AddToEndSingleStrategy::class)
interface AboutView : MvpView {
    fun showAppInfo(appInfo: AppInfo)
    fun showAppDevelopers(devs: List<AppDeveloper>)
}