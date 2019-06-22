package ru.terrakok.gitlabclient.presentation.my.todos

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.TodoListPendingState
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.extension.openInfo
import ru.terrakok.gitlabclient.model.interactor.todo.TodoInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.presentation.global.Paginator
import javax.inject.Inject

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 27.09.17
 */
@InjectViewState
class MyTodosPresenter @Inject constructor(
    @TodoListPendingState private val pendingStateWrapper: PrimitiveWrapper<Boolean>,
    private val todoInteractor: TodoInteractor,
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter
) : BasePresenter<MyTodoListView>() {

    private val isPending = pendingStateWrapper.value
    private var pageDisposable: Disposable? = null
    private val paginator = Paginator.Store<TargetHeader>().apply {
        render = { viewState.renderPaginatorState(it) }
        sideEffectListener = { effect ->
            when (effect) {
                is Paginator.SideEffect.LoadPage -> loadNewPage(effect.currentPage)
            }
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshTodos()
        todoInteractor.todoChanges
            .subscribe { paginator.proceed(Paginator.Action.Refresh) }
            .connect()
    }

    private fun loadNewPage(currentPage: Int) {
        pageDisposable?.dispose()
        pageDisposable =
            todoInteractor.getMyTodos(isPending, currentPage + 1)
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
                        paginator.proceed(Paginator.Action.NewPage(data))
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