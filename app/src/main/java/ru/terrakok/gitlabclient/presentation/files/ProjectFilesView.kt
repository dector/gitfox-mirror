package ru.terrakok.gitlabclient.presentation.files

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.app.ProjectFile

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 02.11.18
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectFilesView : MvpView {

    fun showRefreshProgress(show: Boolean)
    fun showEmptyProgress(show: Boolean)
    fun showPageProgress(show: Boolean)
    fun showEmptyView(show: Boolean)
    fun showEmptyError(show: Boolean, message: String?)
    fun showFiles(show: Boolean, files: List<ProjectFile>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}