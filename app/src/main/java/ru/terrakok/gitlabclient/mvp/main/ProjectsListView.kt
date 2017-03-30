package ru.terrakok.gitlabclient.mvp.main

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.Project

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.03.17
 */

@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectsListView : MvpView {
    fun clearData()
    fun setNewData(projects: List<Project>)
    fun showProgress(isVisible: Boolean)
    fun showPageProgress(isVisible: Boolean)
}