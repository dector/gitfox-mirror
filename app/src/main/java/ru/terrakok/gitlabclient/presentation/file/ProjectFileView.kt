package ru.terrakok.gitlabclient.presentation.file

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 22.11.18.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectFileView : MvpView {

    fun setTitle(title: String)
    fun setRawFile(rawFile: String)
    fun showEmptyView(show: Boolean)
}
