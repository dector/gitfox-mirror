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
    val todoChanges = interactor.todoChanges.wrap()

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

    fun markPendingTodoAsDone(
        id: Long
    ) = promise {
        interactor.markPendingTodoAsDone(id)
    }

    fun markAllPendingTodosAsDone() = promise {
        interactor.markAllPendingTodosAsDone()
    }
}