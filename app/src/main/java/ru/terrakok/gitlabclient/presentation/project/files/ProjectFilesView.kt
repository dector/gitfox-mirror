package ru.terrakok.gitlabclient.presentation.project.files

import gitfox.entity.Branch
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
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
