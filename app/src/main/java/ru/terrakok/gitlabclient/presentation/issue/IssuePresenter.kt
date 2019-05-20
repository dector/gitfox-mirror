package ru.terrakok.gitlabclient.presentation.issue

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.IssueId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.issue.Issue
import ru.terrakok.gitlabclient.model.interactor.issue.IssueInteractor
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 01.11.18.
 */
@InjectViewState
class IssuePresenter @Inject constructor(
    @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
    @IssueId issueIdWrapper: PrimitiveWrapper<Long>,
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

        Single
            .zip(
                issueInteractor.getIssue(projectId, issueId),
                projectInteractor.getProject(projectId),
                BiFunction<Issue, Project, Pair<Issue, Project>> { issue, project ->
                    Pair(issue, project)
                }
            )
            .doOnSubscribe { viewState.showBlockingProgress(true) }
            .doAfterTerminate { viewState.showBlockingProgress(false) }
            .subscribe(
                { (issue, project) ->
                    viewState.setTitle(resourceManager.getString(R.string.issue_title, issue.iid), project.name)
                },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
    }

    fun onBackPressed() = flowRouter.exit()
}