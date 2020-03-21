package ru.terrakok.gitlabclient.presentation.project.events

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.model.interactor.EventInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.util.openInfo
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
@InjectViewState
class ProjectEventsPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    private val eventInteractor: EventInteractor,
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter,
    private val paginator: Paginator.Store<TargetHeader>
) : BasePresenter<ProjectEventsView>() {

    private val projectId = projectIdWrapper.value
    private var pageJob: Job? = null

    init {
        paginator.render = { viewState.renderPaginatorState(it) }
        paginator.sideEffects.subscribe { effect ->
            when (effect) {
                is Paginator.SideEffect.LoadPage -> loadNewPage(effect.currentPage)
                is Paginator.SideEffect.ErrorEvent -> {
                    errorHandler.proceed(effect.error) { viewState.showMessage(it) }
                }
            }
        }.connect()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshEvents()
    }

    private fun loadNewPage(page: Int) {
        pageJob?.cancel()
        pageJob = launch {
            try {
                val data = eventInteractor.getProjectEvents(projectId, page = page)
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

    fun onItemClick(item: TargetHeader.Public) = item.openInfo(router)
    fun refreshEvents() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextEventsPage() = paginator.proceed(Paginator.Action.LoadMore)
    fun onBackPressed() = router.exit()
}
