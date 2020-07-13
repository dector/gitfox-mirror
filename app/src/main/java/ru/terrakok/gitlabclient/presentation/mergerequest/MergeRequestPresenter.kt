package ru.terrakok.gitlabclient.presentation.mergerequest

import gitfox.entity.app.target.TargetAction
import gitfox.model.interactor.MergeRequestInteractor
import gitfox.model.interactor.ProjectInteractor
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.MergeRequestId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.system.ResourceManager
import ru.terrakok.gitlabclient.system.flow.FlowRouter
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 27.11.18.
 */
@InjectViewState
class MergeRequestPresenter @Inject constructor(
    @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
    @MergeRequestId mrIdWrapper: PrimitiveWrapper<Long>,
    private val targetAction: TargetAction,
    private val mrInteractor: MergeRequestInteractor,
    private val projectInteractor: ProjectInteractor,
    private val resourceManager: ResourceManager,
    private val errorHandler: ErrorHandler,
    private val flowRouter: FlowRouter
) : BasePresenter<MergeRequestView>() {

    private val projectId = projectIdWrapper.value
    private val mrId = mrIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        updateToolbarTitle()
        selectActionTab()
    }

    private fun updateToolbarTitle() {
        launch {
            viewState.showBlockingProgress(true)
            try {
                val mr = async { mrInteractor.getMergeRequest(projectId, mrId) }
                val project = projectInteractor.getProject(projectId)
                viewState.setTitle(
                    resourceManager.getString(
                        R.string.merge_request_title,
                        mr.await().iid
                    ), project.name
                )
            } catch (e: Exception) {
                errorHandler.proceed(e) { viewState.showMessage(it) }
            }
            viewState.showBlockingProgress(false)
        }
    }

    private fun selectActionTab() {
        viewState.selectActionTab(targetAction)
    }

    fun onBackPressed() = flowRouter.exit()
}
