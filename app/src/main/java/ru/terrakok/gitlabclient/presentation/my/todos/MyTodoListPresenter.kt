package ru.terrakok.gitlabclient.presentation.my.todos

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.gitlabclient.entity.todo.Todo
import ru.terrakok.gitlabclient.extension.userMessage
import ru.terrakok.gitlabclient.model.interactor.todo.TodoListInteractor
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.TodoListPendingState
import javax.inject.Inject

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 27.09.17
 */
@InjectViewState
class MyTodoListPresenter @Inject constructor(
        private @TodoListPendingState val pendingStateWrapper: PrimitiveWrapper<Boolean>,
        private val todoListInteractor: TodoListInteractor,
        private val resourceManager: ResourceManager
) : MvpPresenter<MyTodoListView>() {

    private val isPending = pendingStateWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshTodos()
    }

    private val paginator = Paginator(
            { todoListInteractor.getMyTodos(isPending, it) },
            object : Paginator.ViewController<Todo> {
                override fun showEmptyProgress(show: Boolean) {
                    viewState.showEmptyProgress(show)
                }

                override fun showEmptyError(show: Boolean, error: Throwable?) {
                    viewState.showEmptyError(show, error?.userMessage(resourceManager))
                }

                override fun showEmptyView(show: Boolean) {
                    viewState.showEmptyView(show)
                }

                override fun showData(show: Boolean, data: List<Todo>) {
                    viewState.showTodos(show, data)
                }

                override fun showErrorMessage(error: Throwable) {
                    viewState.showMessage(error.userMessage(resourceManager))
                }

                override fun showRefreshProgress(show: Boolean) {
                    viewState.showRefreshProgress(show)
                }

                override fun showPageProgress(show: Boolean) {
                    viewState.showPageProgress(show)
                }
            }
    )

    fun refreshTodos() = paginator.refresh()

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
    }
}