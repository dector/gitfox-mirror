package ru.terrakok.gitlabclient.presentation.my.todos

import gitfox.entity.app.target.TargetHeader
import gitfox.model.interactor.AccountInteractor
import gitfox.model.interactor.TodoInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.TodoListPendingState
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.system.flow.FlowRouter
import ru.terrakok.gitlabclient.util.openInfo
import javax.inject.Inject

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 27.09.17
 */
@InjectViewState
class MyTodosPresenter @Inject constructor(
    @TodoListPendingState private val pendingStateWrapper: PrimitiveWrapper<Boolean>,
    private val accountInteractor: AccountInteractor,
    private val todoInteractor: TodoInteractor,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter,
    private val paginator: Paginator.Store<TargetHeader>
) : BasePresenter<MyTodoListView>() {

    private val isPending = pendingStateWrapper.value
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

        refreshTodos()
        todoInteractor.todoChanges
            .onEach { paginator.proceed(Paginator.Action.Refresh) }
            .launchIn(this)
    }

    private fun loadNewPage(page: Int) {
        pageJob?.cancel()
        pageJob = launch {
            try {
                val data = accountInteractor.getMyTodos(isPending, page)
                paginator.proceed(Paginator.Action.NewPage(page, data))
            } catch (e: Exception) {
                errorHandler.proceed(e)
                paginator.proceed(Paginator.Action.PageError(e))
            }
        }
    }

    fun onTodoClick(item: TargetHeader.Public) = item.openInfo(router)
    fun refreshTodos() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextTodosPage() = paginator.proceed(Paginator.Action.LoadMore)
}
