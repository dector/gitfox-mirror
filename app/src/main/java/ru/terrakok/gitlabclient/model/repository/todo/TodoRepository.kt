package ru.terrakok.gitlabclient.model.repository.todo

import ru.terrakok.gitlabclient.entity.Assignee
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.entity.app.target.*
import ru.terrakok.gitlabclient.entity.target.TargetState
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
        currentUser: User,
        action: TodoAction? = null,
        authorId: Long? = null,
        projectId: Long? = null,
        state: TodoState? = null,
        targetType: TargetType? = null,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = api
        .getTodos(action, authorId, projectId, state, targetType, page, pageSize)
        .map { todos -> todos.map { getTargetHeader(it, currentUser) } }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    private fun getTargetHeader(todo: Todo, currentUser: User): TargetHeader {
        val target = todo.target
        val assignee = if (todo.actionName != TodoAction.MARKED) {
            currentUser.let {
                Assignee(it.id, it.state, it.name, it.webUrl, it.avatarUrl, it.username)
            }
        } else {
            null
        }
        val appTarget = when (todo.targetType) {
            TargetType.MERGE_REQUEST -> AppTarget.MERGE_REQUEST
            TargetType.ISSUE -> AppTarget.ISSUE
        }
        val targetName = when (todo.targetType) {
            TargetType.MERGE_REQUEST -> "${AppTarget.MERGE_REQUEST} !${target.iid}"
            TargetType.ISSUE -> "${AppTarget.ISSUE} #${target.iid}"
        }
        val badges = mutableListOf<TargetBadge>()
        badges.add(
            TargetBadge.Status(
                when (target.state) {
                    TargetState.OPENED -> TargetBadgeStatus.OPENED
                    TargetState.CLOSED -> TargetBadgeStatus.CLOSED
                    TargetState.MERGED -> TargetBadgeStatus.MERGED
                }
            )
        )
        badges.add(TargetBadge.Text(todo.author.username, AppTarget.USER, todo.author.id))
        badges.add(TargetBadge.Text(todo.project.name, AppTarget.PROJECT, todo.project.id))

        return TargetHeader.Public(
            todo.author,
            TargetHeaderIcon.NONE,
            TargetHeaderTitle.Todo(
                todo.author.name,
                assignee?.name,
                todo.actionName,
                targetName,
                todo.project.nameWithNamespace,
                todo.author.id == currentUser.id,
                assignee?.id == currentUser.id
            ),
            todo.body,
            todo.createdAt,
            appTarget,
            target.id,
            TargetInternal(target.projectId, target.iid),
            badges
        )
    }

    fun markPendingTodoAsDone(id: Int) = api.markPendingTodoAsDone(id)

    fun markAllPendingTodosAsDone() = api.markAllPendingTodosAsDone()
}