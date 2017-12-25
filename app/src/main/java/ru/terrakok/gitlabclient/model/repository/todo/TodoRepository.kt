package ru.terrakok.gitlabclient.model.repository.todo

import ru.terrakok.gitlabclient.entity.Assignee
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.entity.app.target.AppTarget
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.app.target.TargetHeaderIcon
import ru.terrakok.gitlabclient.entity.app.target.TargetHeaderTitle
import ru.terrakok.gitlabclient.entity.app.user.MyUserInfo
import ru.terrakok.gitlabclient.entity.target.TargetType
import ru.terrakok.gitlabclient.entity.todo.Todo
import ru.terrakok.gitlabclient.entity.todo.TodoAction
import ru.terrakok.gitlabclient.entity.todo.TodoState
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultPageSize
import javax.inject.Inject

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 20.09.17
 */
class TodoRepository @Inject constructor(
        private val api: GitlabApi,
        private val schedulers: SchedulersProvider,
        @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) {
    private val defaultPageSize = defaultPageSizeWrapper.value

    fun getTodos(
            myUserInfo: MyUserInfo,
            action: TodoAction? = null,
            authorId: Long? = null,
            projectId: Long? = null,
            state: TodoState? = null,
            targetType: TargetType? = null,
            page: Int,
            pageSize: Int = defaultPageSize
    ) = api
            .getTodos(action, authorId, projectId, state, targetType, page, pageSize)
            .map { todos ->
                val items = mutableListOf<TargetHeader>()
                todos.forEach {
                    items.add(getTargetHeader(it, myUserInfo.user))
                }
                return@map items.toList()
            }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    private fun getTargetHeader(todo: Todo, currentUser: User?): TargetHeader {
        val assignee = getAssignee(todo, currentUser)
        val appTarget = when (todo.targetType) {
            TargetType.MERGE_REQUEST -> AppTarget.MERGE_REQUEST
            TargetType.ISSUE -> AppTarget.ISSUE
        }

        return TargetHeader(
                todo.author,
                TargetHeaderIcon.NONE,
                TargetHeaderTitle.Todo(
                        todo.author.let { it.name ?: it.username } ?: "user",
                        assignee?.let { it.name ?: it.username },
                        todo.actionName,
                        when (todo.targetType) {
                            TargetType.MERGE_REQUEST -> "${AppTarget.MERGE_REQUEST} !${todo.target.iid}"
                            TargetType.ISSUE -> "${AppTarget.ISSUE} #${todo.target.iid}"
                        },
                        todo.project.nameWithNamespace ?: "project",
                        todo.author.id == currentUser?.id,
                        assignee?.id == currentUser?.id
                ),
                todo.body,
                todo.createdAt,
                appTarget,
                todo.target.id
        )
    }

    private fun getAssignee(todo: Todo, currentUser: User?): Assignee? {
        return if (todo.actionName != TodoAction.MARKED) {
            currentUser?.let {
                Assignee(it.id, it.state, it.name, it.webUrl, it.avatarUrl, it.username)
            }
        } else {
            null
        }
    }

    fun markPendingTodoAsDone(id: Int) = api.markPendingTodoAsDone(id)

    fun markAllPendingTodosAsDone() = api.markAllPendingTodosAsDone()
}