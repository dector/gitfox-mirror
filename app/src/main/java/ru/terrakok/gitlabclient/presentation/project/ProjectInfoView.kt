package ru.terrakok.gitlabclient.presentation.project

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.Project

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 27.04.17.
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectInfoView : MvpView {
    fun showProjectInfo(project: Project)
    fun showProgress(show: Boolean)
    fun showReadmeFile(rawFile: String)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}