package ru.terrakok.gitlabclient.presentation.libraries

import gitfox.entity.app.develop.AppLibrary
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 23.12.17.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface LibrariesView : MvpView {
    fun showLibraries(libraries: List<AppLibrary>)
}
