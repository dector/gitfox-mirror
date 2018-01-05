package ru.terrakok.gitlabclient.model.repository.todo

import ru.terrakok.gitlabclient.entity.Assignee
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.entity.app.target.*
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
            .map { todos -> todos.map { getTargetHeader(it, myUserInfo.user) } }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    private fun getTargetHeader(todo: Todo, currentUser: User): TargetHeader {
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
            TargetType.MERGE_REQUEST -> "${AppTarget.MERGE_REQUEST} !${todo.target.iid}"
            TargetType.ISSUE -> "${AppTarget.ISSUE} #${todo.target.iid}"
        }
        val badges = mutableListOf<TargetBadge>()
        badges.add(TargetBadge.Text(todo.project.name, AppTarget.PROJECT, todo.project.id))

        return TargetHeader(
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
                todo.target.id,
                badges
        )
    }

    fun markPendingTodoAsDone(id: Int) = api.markPendingTodoAsDone(id)

    fun markAllPendingTodosAsDone() = api.markAllPendingTodosAsDone()
}