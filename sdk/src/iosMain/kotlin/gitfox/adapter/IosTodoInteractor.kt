package gitfox.adapter

import gitfox.entity.*
import gitfox.entity.app.target.TargetHeader
import gitfox.model.interactor.TodoInteractor
import kotlinx.coroutines.CoroutineScope

class IosTodoInteractor internal constructor(
    private val interactor: TodoInteractor,
    private val defaultPageSize: Int
) : CoroutineScope by CoroutineScope(MainLoopDispatcher) {
    //    val todoChanges: Flow<Long>

    fun getTodos(
        currentUser: User,
        action: TodoAction? = null,
        authorId: Long? = null,
        projectId: Long? = null,
        state: TodoState? = null,
        targetType: TargetType? = null,
        page: Int,
        pageSize: Int = defaultPageSize,
        callback: (result: List<TargetHeader>?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getTodos(currentUser, action, authorId, projectId, state, targetType, page, pageSize) }
    }

    fun markPendingTodoAsDone(
        id: Long,
        callback: (result: Todo?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.markPendingTodoAsDone(id) }
    }

    fun markAllPendingTodosAsDone(
        callback: (result: Unit?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.markAllPendingTodosAsDone() }
    }
}