package ru.terrakok.gitlabclient.presentation.mergerequest

import moxy.InjectViewState
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.MergeRequestId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.app.target.TargetAction
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest
import ru.terrakok.gitlabclient.model.interactor.MergeRequestInteractor
import ru.terrakok.gitlabclient.model.interactor.ProjectInteractor
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
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
        Single
            .zip(
                mrInteractor.getMergeRequest(projectId, mrId),
                projectInteractor.getProject(projectId),
                BiFunction<MergeRequest, Project, Pair<MergeRequest, Project>> { mr, project ->
                    Pair(mr, project)
                }
            )
            .doOnSubscribe { viewState.showBlockingProgress(true) }
            .doAfterTerminate { viewState.showBlockingProgress(false) }
            .subscribe(
                { (mr, project) ->
                    viewState.setTitle(resourceManager.getString(R.string.merge_request_title, mr.iid), project.name)
                },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
    }

    private fun selectActionTab() {
        viewState.selectActionTab(targetAction)
    }

    fun onBackPressed() = flowRouter.exit()
}