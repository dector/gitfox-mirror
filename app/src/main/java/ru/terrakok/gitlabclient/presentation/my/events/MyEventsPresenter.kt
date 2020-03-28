package ru.terrakok.gitlabclient.presentation.my.events

import gitfox.entity.app.target.TargetHeader
import gitfox.model.interactor.EventInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.*
import ru.terrakok.gitlabclient.util.openInfo
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
@InjectViewState
class MyEventsPresenter @Inject constructor(
    private val eventInteractor: EventInteractor,
    private val mdConverter: MarkDownConverter,
    private val menuController: GlobalMenuController,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter,
    private val paginator: Paginator.Store<TargetHeader>
) : BasePresenter<MyEventsView>() {

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
                val data = eventInteractor.getEvents(page = page)
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

    fun onMenuClick() = menuController.open()
    fun onItemClick(item: TargetHeader.Public) = item.openInfo(router)
    fun refreshEvents() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextEventsPage() = paginator.proceed(Paginator.Action.LoadMore)
    fun onBackPressed() = router.exit()
}
