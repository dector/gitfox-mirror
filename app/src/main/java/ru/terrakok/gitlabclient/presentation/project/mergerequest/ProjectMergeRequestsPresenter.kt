package ru.terrakok.gitlabclient.presentation.project.mergerequest

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.MergeRequestState
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.model.interactor.MergeRequestInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.util.openInfo
import javax.inject.Inject

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 28.08.18
 */
@InjectViewState
class ProjectMergeRequestsPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    private val mergeRequestState: MergeRequestState,
    private val mergeRequestInteractor: MergeRequestInteractor,
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter,
    private val paginator: Paginator.Store<TargetHeader>
) : BasePresenter<ProjectMergeRequestsView>() {

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

        refreshMergeRequests()
        mergeRequestInteractor.mergeRequestChanges
            .onEach { paginator.proceed(Paginator.Action.Refresh) }
            .launchIn(this)
    }

    private fun loadNewPage(page: Int) {
        pageJob?.cancel()
        pageJob = launch {
            try {
                val data = mergeRequestInteractor.getMergeRequests(
                        projectId,
                        mergeRequestState,
                        page = page
                    )
                    .map { item ->
                        when (item) {
                            is TargetHeader.Public ->
                                item.copy(body = mdConverter.toSpannable(item.body.toString()))
                            is TargetHeader.Confidential -> item
                        }
                    }
                paginator.proceed(Paginator.Action.NewPage(page, data))
            } catch (e: Exception) {
                errorHandler.proceed(e)
                paginator.proceed(Paginator.Action.PageError(e))
            }
        }
    }

    fun onMergeRequestClick(item: TargetHeader.Public) = item.openInfo(router)
    fun refreshMergeRequests() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextMergeRequestsPage() = paginator.proceed(Paginator.Action.LoadMore)
}
