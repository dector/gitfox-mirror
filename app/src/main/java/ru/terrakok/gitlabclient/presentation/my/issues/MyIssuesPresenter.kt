package ru.terrakok.gitlabclient.presentation.my.issues

import gitfox.entity.app.target.TargetHeader
import gitfox.model.interactor.AccountInteractor
import gitfox.model.interactor.IssueInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
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
class MyIssuesPresenter @Inject constructor(
    initFilter: Filter,
    private val accountInteractor: AccountInteractor,
    private val issueInteractor: IssueInteractor,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter,
    private val paginator: Paginator.Store<TargetHeader>
) : BasePresenter<MyIssuesView>() {
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
        refreshIssues()
        issueInteractor.issueChanges
            .onEach { paginator.proceed(Paginator.Action.Refresh) }
            .launchIn(this)
    }

    private fun loadNewPage(page: Int) {
        pageJob?.cancel()
        pageJob = launch {
            try {
                val data = accountInteractor.getMyIssues(filter.createdByMe, filter.onlyOpened, page)
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

    fun onIssueClick(item: TargetHeader.Public) = item.openInfo(router)
    fun refreshIssues() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextIssuesPage() = paginator.proceed(Paginator.Action.LoadMore)
}
