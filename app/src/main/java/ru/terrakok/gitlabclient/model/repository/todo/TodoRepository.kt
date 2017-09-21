package ru.terrakok.gitlabclient.model.repository.todo

import ru.terrakok.gitlabclient.entity.target.TargetType
import ru.terrakok.gitlabclient.entity.todo.TodoAction
import ru.terrakok.gitlabclient.entity.todo.TodoState
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultPageSize
import javax.inject.Inject

class TodoRepository @Inject constructor(
        private val api: GitlabApi,
        private val schedulers: SchedulersProvider,
        @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) {
    private val defaultPageSize = defaultPageSizeWrapper.value

    fun getTodos(
            action: TodoAction? = null,
            authorId: Long? = null,
            projectId: Long? = null,
            state: TodoState? = null,
            targetType: TargetType? = null,
            page: Int,
            pageSize: Int = defaultPageSize
    ) = api
            .getTodos(action, authorId, projectId, state, targetType, page, pageSize)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun markPendingTodoAsDone(id: Int) = api.markPendingTodoAsDone(id)

    fun markAllPendingTodosAsDone() = api.markAllPendingTodosAsDone()
}