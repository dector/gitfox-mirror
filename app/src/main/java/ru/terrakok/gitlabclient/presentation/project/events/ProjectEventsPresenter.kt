package ru.terrakok.gitlabclient.presentation.project.events

import gitfox.entity.app.target.TargetHeader
import gitfox.model.interactor.EventInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
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
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
@InjectViewState
class ProjectEventsPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    private val eventInteractor: EventInteractor,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter,
    private val paginator: Paginator.Store<TargetHeader>
) : BasePresenter<ProjectEventsView>() {

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
        refreshEvents()
    }

    private fun loadNewPage(page: Int) {
        pageJob?.cancel()
        pageJob = launch {
            try {
                val data = eventInteractor.getProjectEvents(projectId, page = page)
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
