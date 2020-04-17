package gitfox.adapter

import gitfox.entity.TargetType
import gitfox.entity.TodoAction
import gitfox.entity.TodoState
import gitfox.entity.User
import gitfox.model.interactor.TodoInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

class JsTodoInteractor internal constructor(
    private val interactor: TodoInteractor,
    private val defaultPageSize: Int
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    @JsName("todoChanges")
    val todoChanges = interactor.todoChanges.wrap()

    @JsName("getTodos")
    fun getTodos(
        currentUser: User,
        action: TodoAction? = null,
        authorId: Long? = null,
        projectId: Long? = null,
        state: TodoState? = null,
        targetType: TargetType? = null,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = promise {
        interactor.getTodos(currentUser, action, authorId, projectId, state, targetType, page, pageSize)
    }

    @JsName("markPendingTodoAsDone")
    fun markPendingTodoAsDone(
        id: Long
    ) = promise {
        interactor.markPendingTodoAsDone(id)
    }

    @JsName("markAllPendingTodosAsDone")
    fun markAllPendingTodosAsDone() = promise {
        interactor.markAllPendingTodosAsDone()
    }
}