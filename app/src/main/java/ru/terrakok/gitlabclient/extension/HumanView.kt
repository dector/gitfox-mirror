package ru.terrakok.gitlabclient.extension

import android.content.Context
import android.content.res.Resources
import android.support.annotation.DrawableRes
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import retrofit2.HttpException
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.develop.LicenseType
import ru.terrakok.gitlabclient.entity.app.target.TargetBadgeStatus
import ru.terrakok.gitlabclient.entity.app.target.TargetHeaderIcon
import ru.terrakok.gitlabclient.entity.app.target.TargetHeaderTitle
import ru.terrakok.gitlabclient.entity.event.EventAction
import ru.terrakok.gitlabclient.entity.todo.TodoAction
import ru.terrakok.gitlabclient.model.system.ResourceManager
import timber.log.Timber
import java.io.IOException

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 14.02.18.
 */

fun Throwable.userMessage(resourceManager: ResourceManager) = when (this) {
    is HttpException -> when (this.code()) {
        304 -> resourceManager.getString(R.string.not_modified_error)
        400 -> resourceManager.getString(R.string.bad_request_error)
        401 -> resourceManager.getString(R.string.unauthorized_error)
        403 -> resourceManager.getString(R.string.forbidden_error)
        404 -> resourceManager.getString(R.string.not_found_error)
        405 -> resourceManager.getString(R.string.method_not_allowed_error)
        409 -> resourceManager.getString(R.string.conflict_error)
        422 -> resourceManager.getString(R.string.unprocessable_error)
        500 -> resourceManager.getString(R.string.server_error_error)
        else -> resourceManager.getString(R.string.unknown_error)
    }
    is IOException -> resourceManager.getString(R.string.network_error)
    else -> resourceManager.getString(R.string.unknown_error)
}

fun LocalDateTime.humanTime(resources: Resources): String {
    val delta = Duration.between(this, LocalDateTime.now()).seconds
    val timeStr =
            when {
                delta < 60 -> resources.getString(R.string.time_sec, delta)
                delta < 60 * 60 -> resources.getString(R.string.time_min, delta / 60)
                delta < 60 * 60 * 24 -> resources.getString(R.string.time_hour, delta / (60 * 60))
                delta < 60 * 60 * 24 * 7 -> resources.getString(R.string.time_day, delta / (60 * 60 * 24))
                else -> return this.toLocalDate().toString()
            }

    return resources.getString(R.string.time_ago, timeStr)
}

fun EventAction.getHumanName(resources: Resources) = when (this) {
    EventAction.UPDATED -> resources.getString(R.string.event_action_updated)
    EventAction.REOPENED -> resources.getString(R.string.event_action_reopened)
    EventAction.PUSHED_TO -> resources.getString(R.string.event_action_pushed_to)
    EventAction.PUSHED_NEW -> resources.getString(R.string.event_action_pushed_new)
    EventAction.PUSHED -> resources.getString(R.string.event_action_pushed)
    EventAction.LEFT -> resources.getString(R.string.event_action_left)
    EventAction.OPENED -> resources.getString(R.string.event_action_opened)
    EventAction.DESTROYED -> resources.getString(R.string.event_action_destroyed)
    EventAction.DELETED -> resources.getString(R.string.event_action_deleted)
    EventAction.EXPIRED -> resources.getString(R.string.event_action_expired)
    EventAction.MERGED -> resources.getString(R.string.event_action_merged)
    EventAction.CLOSED -> resources.getString(R.string.event_action_closed)
    EventAction.ACCEPTED -> resources.getString(R.string.event_action_accepted)
    EventAction.COMMENTED -> resources.getString(R.string.event_action_commented)
    EventAction.COMMENTED_ON -> resources.getString(R.string.event_action_commented_on)
    EventAction.JOINED -> resources.getString(R.string.event_action_joined)
    EventAction.CREATED -> resources.getString(R.string.event_action_created)
}

@DrawableRes
fun TargetHeaderIcon.getIcon() = when (this) {
    TargetHeaderIcon.CREATED -> R.drawable.ic_event_created_24dp
    TargetHeaderIcon.JOINED -> R.drawable.ic_event_joined_24dp
    TargetHeaderIcon.COMMENTED -> R.drawable.ic_event_commented_24dp
    TargetHeaderIcon.MERGED -> R.drawable.ic_event_merged_24dp
    TargetHeaderIcon.CLOSED -> R.drawable.ic_event_closed_24dp
    TargetHeaderIcon.DESTROYED -> R.drawable.ic_event_destroyed_24dp
    TargetHeaderIcon.EXPIRED -> R.drawable.ic_event_expired_24dp
    TargetHeaderIcon.LEFT -> R.drawable.ic_event_left_24dp
    TargetHeaderIcon.REOPENED -> R.drawable.ic_event_reopened_24dp
    TargetHeaderIcon.PUSHED -> R.drawable.ic_event_pushed_24dp
    TargetHeaderIcon.UPDATED -> R.drawable.ic_event_updated_24dp
    TargetHeaderIcon.NONE -> R.drawable.ic_event_created_24dp
}

fun TodoAction.getHumanName(resources: Resources): String = when (this) {
    TodoAction.APPROVAL_REQUIRED -> resources.getString(R.string.todo_action_approval_required)
    TodoAction.ASSIGNED -> resources.getString(R.string.todo_action_assigned)
    TodoAction.BUILD_FAILED -> resources.getString(R.string.todo_action_build_failed)
    TodoAction.DIRECTLY_ADDRESSED -> resources.getString(R.string.todo_action_directly_addressed)
    TodoAction.MARKED -> resources.getString(R.string.todo_action_marked)
    TodoAction.MENTIONED -> resources.getString(R.string.todo_action_mentioned)
}

fun TargetHeaderTitle.getHumanName(resources: Resources) = when (this) {
    is TargetHeaderTitle.Event -> {
        "$userName ${action.getHumanName(resources)} $targetName ${resources.getString(R.string.at)} $projectName"
    }
    is TargetHeaderTitle.Todo -> {
        val actionName = action.getHumanName(resources)
        val author = if (isAuthorCurrentUser) {
            resources.getString(R.string.you).capitalize()
        } else {
            authorUserName
        }
        val assignee = if (isAssigneeCurrentUser) {
            if (isAuthorCurrentUser) {
                resources.getString(R.string.yourself)
            } else {
                resources.getString(R.string.you)
            }
        } else {
            assigneeUserName
        }

        when (action) {
            TodoAction.ASSIGNED -> {
                "$author $actionName $targetName ${resources.getString(R.string.at)} $projectName ${resources.getString(R.string.to)} $assignee"
            }
            TodoAction.DIRECTLY_ADDRESSED,
            TodoAction.MENTIONED -> {
                "$author $actionName $assignee ${resources.getString(R.string.on)} $targetName ${resources.getString(R.string.at)} $projectName"
            }
            TodoAction.MARKED -> {
                "$author $actionName ${resources.getString(R.string.for_str)} $targetName ${resources.getString(R.string.at)} $projectName"
            }
            else -> {
                Timber.e("Unsupported template for todo action=$actionName.")
                "$author $actionName $targetName $assignee ${resources.getString(R.string.at)}"
            }
        }
    }
}

fun LicenseType.getHumanName(resources: Resources) = when (this) {
    LicenseType.MIT -> resources.getString(R.string.library_license_MIT)
    LicenseType.APACHE_2 -> resources.getString(R.string.library_license_APACHE_2)
    LicenseType.CUSTOM -> resources.getString(R.string.library_license_CUSTOM)
    LicenseType.NONE -> resources.getString(R.string.library_license_NONE)
}

fun TargetBadgeStatus.getHumanName(resources: Resources) = when (this) {
    TargetBadgeStatus.OPENED -> resources.getString(R.string.target_status_opened)
    TargetBadgeStatus.CLOSED -> resources.getString(R.string.target_status_closed)
    TargetBadgeStatus.MERGED -> resources.getString(R.string.target_status_merged)
}

fun TargetBadgeStatus.getBadgeColors(context: Context) = when (this) {
    TargetBadgeStatus.OPENED -> Pair(context.color(R.color.green), context.color(R.color.lightGreen))
    TargetBadgeStatus.CLOSED -> Pair(context.color(R.color.red), context.color(R.color.lightRed))
    TargetBadgeStatus.MERGED -> Pair(context.color(R.color.blue), context.color(R.color.lightBlue))
}