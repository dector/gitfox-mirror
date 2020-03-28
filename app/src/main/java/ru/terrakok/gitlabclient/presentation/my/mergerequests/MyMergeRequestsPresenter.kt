package ru.terrakok.gitlabclient.presentation.my.mergerequests

import gitfox.entity.app.target.TargetHeader
import gitfox.model.interactor.AccountInteractor
import gitfox.model.interactor.MergeRequestInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.system.flow.FlowRouter
import ru.terrakok.gitlabclient.util.openInfo
import javax.inject.Inject

@InjectViewState
class MyMergeRequestsPresenter @Inject constructor(
    initFilter: Filter,
    private val interactor: AccountInteractor,
    private val mrInteractor: MergeRequestInteractor,
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter,
    private val paginator: Paginator.Store<TargetHeader>
) : BasePresenter<MyMergeRequestListView>() {
    data class Filter(val createdByMe: Boolean, val onlyOpened: Boolean)

    private var filter = initFilter
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
        mrInteractor.mergeRequestChanges
            .onEach { paginator.proceed(Paginator.Action.Refresh) }
            .launchIn(this)
    }

    private fun loadNewPage(page: Int) {
        pageJob?.cancel()
        pageJob = launch {
            try {
                val data =
                    interactor.getMyMergeRequests(filter.createdByMe, filter.onlyOpened, page)
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

    fun applyNewFilter(filter: Filter) {
        if (this.filter != filter) {
            this.filter = filter
            paginator.proceed(Paginator.Action.Restart)
        }
    }

    fun onMergeRequestClick(item: TargetHeader.Public) = item.openInfo(router)
    fun refreshMergeRequests() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextMergeRequestsPage() = paginator.proceed(Paginator.Action.LoadMore)
}
