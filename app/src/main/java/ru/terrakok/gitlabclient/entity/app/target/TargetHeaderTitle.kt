package ru.terrakok.gitlabclient.entity.app.target

import ru.terrakok.gitlabclient.entity.event.EventAction
import ru.terrakok.gitlabclient.entity.todo.TodoAction

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 24.12.17.
 */
sealed class TargetHeaderTitle {
    data class Event(
            val userName: String,
            val action: EventAction,
            val targetName: String,
            val projectName: String
    ) : TargetHeaderTitle()

    data class Todo(
            val authorUserName: String,
            val assigneeUserName: String?,
            val action: TodoAction,
            val targetName: String,
            val projectName: String,
            val isAuthorCurrentUser: Boolean,
            val isAssigneeCurrentUser: Boolean
    ) : TargetHeaderTitle()
}