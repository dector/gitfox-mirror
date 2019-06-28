package ru.terrakok.gitlabclient.presentation.project.files

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.terrakok.gitlabclient.entity.Branch
import ru.terrakok.gitlabclient.presentation.global.Paginator

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 02.11.18
 */
@StateStrategyType(AddToEndSingleStrategy::class)
interface ProjectFilesView : MvpView {

    fun setPath(path: String)
    fun setBranch(branchName: String)
    fun showBlockingProgress(show: Boolean)
    fun showBranchSelection(show: Boolean)

    fun renderPaginatorState(state: Paginator.State)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showBranches(branches: List<Branch>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMessage(message: String)
}