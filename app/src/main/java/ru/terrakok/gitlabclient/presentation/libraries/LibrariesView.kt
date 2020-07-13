package ru.terrakok.gitlabclient.presentation.libraries

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.system.AppLibrary

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 23.12.17.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface LibrariesView : MvpView {
    fun showLibraries(libraries: List<AppLibrary>)
}
