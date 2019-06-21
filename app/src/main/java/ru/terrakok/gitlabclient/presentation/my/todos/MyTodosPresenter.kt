package ru.terrakok.gitlabclient.presentation.my.todos

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.Screens
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
    private val router: FlowRouter
) : BasePresenter<MyTodoListView>() {

    private val isPending = pendingStateWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshTodos()
    }

    private val paginator = Paginator(
        { todoInteractor.getMyTodos(isPending, it) },todoInteractor.todoChanges,
        object :
            Paginator.ViewController<TargetHeader> {
            override fun showEmptyProgress(show: Boolean) {
                viewState.showEmptyProgress(show)
            }

            override fun showEmptyError(show: Boolean, error: Throwable?) {
                if (error != null) {
                    errorHandler.proceed(error, { viewState.showEmptyError(show, it) })
                } else {
                    viewState.showEmptyError(show, null)
                }
            }

            override fun showEmptyView(show: Boolean) {
                viewState.showEmptyView(show)
            }

            override fun showData(show: Boolean, data: List<TargetHeader>) {
                viewState.showTodos(show, data)
            }

            override fun showErrorMessage(error: Throwable) {
                errorHandler.proceed(error, { viewState.showMessage(it) })
            }

            override fun showRefreshProgress(show: Boolean) {
                viewState.showRefreshProgress(show)
            }

            override fun showPageProgress(show: Boolean) {
                viewState.showPageProgress(show)
            }
        }
    )

    fun onTodoClick(item: TargetHeader.Public) = item.openInfo(router)
    fun onUserClick(userId: Long) = router.startFlow(Screens.UserFlow(userId))
    fun refreshTodos() = paginator.refresh()
    fun loadNextTodosPage() = paginator.loadNewPage()

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
    }
}