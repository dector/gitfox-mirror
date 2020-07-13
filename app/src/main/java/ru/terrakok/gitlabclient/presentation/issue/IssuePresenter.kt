package ru.terrakok.gitlabclient.presentation.issue

import gitfox.entity.app.target.TargetAction
import gitfox.model.interactor.IssueInteractor
import gitfox.model.interactor.ProjectInteractor
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.IssueId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.system.ResourceManager
import ru.terrakok.gitlabclient.system.flow.FlowRouter
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 01.11.18.
 */
@InjectViewState
class IssuePresenter @Inject constructor(
    @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
    @IssueId issueIdWrapper: PrimitiveWrapper<Long>,
    private val targetAction: TargetAction,
    private val issueInteractor: IssueInteractor,
    private val projectInteractor: ProjectInteractor,
    private val resourceManager: ResourceManager,
    private val errorHandler: ErrorHandler,
    private val flowRouter: FlowRouter
) : BasePresenter<IssueView>() {

    private val projectId = projectIdWrapper.value
    private val issueId = issueIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        updateToolbarTitle()
        selectActionTab()
    }

    private fun updateToolbarTitle() {
        launch {
            viewState.showBlockingProgress(true)
            try {
                val issue = async { issueInteractor.getIssue(projectId, issueId) }
                val project = projectInteractor.getProject(projectId)
                viewState.setTitle(
                    resourceManager.getString(R.string.issue_title, issue.await().iid),
                    project.name
                )
            } catch (e: Exception) {

            }
            viewState.showBlockingProgress(false)
        }
    }

    private fun selectActionTab() {
        viewState.selectActionTab(targetAction)
    }

    fun onBackPressed() = flowRouter.exit()

    fun onCloseIssueClicked() {
        launch {
            viewState.showBlockingProgress(true)
            try {
                issueInteractor.closeIssue(projectId, issueId)
            } catch (e: Exception) {
                errorHandler.proceed(e) { viewState.showMessage(it) }
            }
            viewState.showBlockingProgress(false)
        }
    }
}
