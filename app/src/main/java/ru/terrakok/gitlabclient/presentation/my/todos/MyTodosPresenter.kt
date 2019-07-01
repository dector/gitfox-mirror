package ru.terrakok.gitlabclient.presentation.my.todos

import com.arellomobile.mvp.InjectViewState
import io.reactivex.disposables.Disposable
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.TodoListPendingState
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.extension.openInfo
import ru.terrakok.gitlabclient.model.interactor.todo.TodoInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import javax.inject.Inject

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 27.09.17
 */
@InjectViewState
class MyTodosPresenter @Inject constructor(
    @TodoListPendingState private val pendingStateWrapper: PrimitiveWrapper<Boolean>,
    private val todoInteractor: TodoInteractor,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter,
    private val paginator: Paginator.Store<TargetHeader>
) : BasePresenter<MyTodoListView>() {

    private val isPending = pendingStateWrapper.value
    private var pageDisposable: Disposable? = null

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

        refreshTodos()
        todoInteractor.todoChanges
            .subscribe { paginator.proceed(Paginator.Action.Refresh) }
            .connect()
    }

    private fun loadNewPage(page: Int) {
        pageDisposable?.dispose()
        pageDisposable =
            todoInteractor.getMyTodos(isPending, page)
                .flattenAsObservable { it }
                .concatMap { item ->
                    when (item) {
                        is TargetHeader.Public -> {
                            mdConverter.markdownToSpannable(item.body.toString())
                                .map { md -> item.copy(body = md) }
                                .toObservable()
                        }
                        is TargetHeader.Confidential -> Observable.just(item)
                    }
                }
                .toList()
                .subscribe(
                    { data ->
                        paginator.proceed(Paginator.Action.NewPage(page, data))
                    },
                    { e ->
                        errorHandler.proceed(e)
                        paginator.proceed(Paginator.Action.PageError(e))
                    }
                )
        pageDisposable?.connect()
    }

    fun onTodoClick(item: TargetHeader.Public) = item.openInfo(router)
    fun refreshTodos() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextTodosPage() = paginator.proceed(Paginator.Action.LoadMore)
}