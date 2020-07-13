package ru.terrakok.gitlabclient.presentation.project.issues

import gitfox.entity.IssueState
import gitfox.entity.app.target.TargetHeader
import gitfox.model.interactor.IssueInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.system.flow.FlowRouter
import ru.terrakok.gitlabclient.util.openInfo
import javax.inject.Inject

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 27.08.18
 */
@InjectViewState
class ProjectIssuesPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    private val issueState: IssueState,
    private val issueInteractor: IssueInteractor,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter,
    private val paginator: Paginator.Store<TargetHeader>
) : BasePresenter<ProjectIssuesView>() {

    private val projectId = projectIdWrapper.value
    private var pageJob: Job? = null

    init {
        paginator.render = { viewState.renderPaginatorState(it) }
        launch {
            paginator.sideEffects.consumeEach { effect ->
                when (effect) {
                    is Paginator.SideEffect.LoadPage -> loadNewPage(effect.currentPage)
                    is Paginator.SideEffect.ErrorEvent -> {
                        errorHandler.proceed(effect.error) { viewState.showMessage(it) }
                    }
                }
            }
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshIssues()
        issueInteractor.issueChanges
            .onEach { paginator.proceed(Paginator.Action.Refresh) }
            .launchIn(this)
    }

    private fun loadNewPage(page: Int) {
        pageJob?.cancel()
        pageJob = launch {
            try {
                val data = issueInteractor.getIssues(projectId, state = issueState, page = page)
                paginator.proceed(Paginator.Action.NewPage(page, data))
            } catch (e: Exception) {
                errorHandler.proceed(e)
                paginator.proceed(Paginator.Action.PageError(e))
            }
        }
    }

    fun onIssueClick(item: TargetHeader.Public) = item.openInfo(router)
    fun refreshIssues() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextIssuesPage() = paginator.proceed(Paginator.Action.LoadMore)
}
