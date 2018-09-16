package ru.terrakok.gitlabclient.presentation.libraries

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.app.develop.AppLibrary

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 23.12.17.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface LibrariesView : MvpView {
    fun showLibraries(libraries: List<AppLibrary>)
}