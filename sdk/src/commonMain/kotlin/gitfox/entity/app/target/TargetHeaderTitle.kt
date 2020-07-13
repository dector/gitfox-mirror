package gitfox.entity.app.target

import gitfox.entity.EventAction
import gitfox.entity.TodoAction

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
