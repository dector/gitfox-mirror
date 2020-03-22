package ru.terrakok.gitlabclient.model.interactor

import kotlinx.coroutines.flow.Flow
import ru.terrakok.gitlabclient.entity.*
import ru.terrakok.gitlabclient.entity.app.target.*
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 20.09.17
 */
class TodoInteractor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val defaultPageSize: Int
) {

    val todoChanges: Flow<Long> = serverChanges.todoChanges

    suspend fun getTodos(
        currentUser: User,
        action: TodoAction? = null,
        authorId: Long? = null,
        projectId: Long? = null,
        state: TodoState? = null,
        targetType: TargetType? = null,
        page: Int,
        pageSize: Int = defaultPageSize
    ): List<TargetHeader> =
        api.getTodos(action, authorId, projectId, state, targetType, page, pageSize)
            .map { getTargetHeader(it, currentUser) }

    suspend fun markPendingTodoAsDone(id: Long): Todo =
        api.markPendingTodoAsDone(id)

    suspend fun markAllPendingTodosAsDone() {
        api.markAllPendingTodosAsDone()
    }

    private fun getTargetHeader(todo: Todo, currentUser: User): TargetHeader {
        val target = todo.target
        val assignee = if (todo.actionName != TodoAction.MARKED) {
            currentUser.let {
                ShortUser(it.id, it.state, it.name, it.webUrl, it.avatarUrl, it.username)
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
            badges,
            TargetAction.Undefined
        )
    }
}
