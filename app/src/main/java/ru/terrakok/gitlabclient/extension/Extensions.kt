package ru.terrakok.gitlabclient.extension

import android.content.res.Resources
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import retrofit2.HttpException
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.app.FullEventTarget
import ru.terrakok.gitlabclient.entity.event.EventAction
import ru.terrakok.gitlabclient.model.system.ResourceManager
import java.io.IOException
import java.util.*

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 03.03.17
 */
fun Resources.color(colorRes: Int) =
        if (Build.VERSION.SDK_INT >= 23) {
            this.getColor(colorRes, null)
        } else {
            this.getColor(colorRes)
        }

fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

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

private val DATE_FORMAT = DateTimeFormat.forPattern("dd.MM.yyyy")
fun Date.humanTime(resources: Resources): String {
    val localMillis = DateTimeZone.getDefault().convertUTCToLocal(this.time)
    val timeDelta = (System.currentTimeMillis() - localMillis) / 1000L
    val timeStr =
            if (timeDelta < 60) {
                resources.getString(R.string.time_sec, timeDelta)
            } else if (timeDelta < 60 * 60) {
                resources.getString(R.string.time_min, timeDelta / 60)
            } else if (timeDelta < 60 * 60 * 24) {
                resources.getString(R.string.time_hour, timeDelta / (60 * 60))
            } else if (timeDelta < 60 * 60 * 24 * 7) {
                resources.getString(R.string.time_day, timeDelta / (60 * 60 * 24))
            } else {
                return DATE_FORMAT.print(localMillis)
            }

    return resources.getString(R.string.time_ago, timeStr)
}

fun FullEventTarget.getHumanName(resources: Resources) = when (this) {
    FullEventTarget.PROJECT -> resources.getString(R.string.full_event_target_project)
    FullEventTarget.ISSUE -> resources.getString(R.string.full_event_target_issue)
    FullEventTarget.MERGE_REQUEST -> resources.getString(R.string.full_event_target_merge_request)
    FullEventTarget.BRANCH -> resources.getString(R.string.full_event_target_branch)
    FullEventTarget.COMMIT -> resources.getString(R.string.full_event_target_commit)
    FullEventTarget.MILESTONE -> resources.getString(R.string.full_event_target_milestone)
    FullEventTarget.SNIPPET -> resources.getString(R.string.full_event_target_snippet)
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
fun EventAction.getIcon() = when (this) {
    EventAction.CREATED -> R.drawable.ic_event_created_24dp
    EventAction.JOINED -> R.drawable.ic_event_joined_24dp
    EventAction.COMMENTED_ON, EventAction.COMMENTED -> R.drawable.ic_event_commented_24dp
    EventAction.MERGED, EventAction.ACCEPTED -> R.drawable.ic_event_merged_24dp
    EventAction.CLOSED -> R.drawable.ic_event_closed_24dp
    EventAction.DELETED, EventAction.DESTROYED -> R.drawable.ic_event_destroyed_24dp
    EventAction.EXPIRED -> R.drawable.ic_event_expired_24dp
    EventAction.LEFT -> R.drawable.ic_event_left_24dp
    EventAction.OPENED, EventAction.REOPENED -> R.drawable.ic_event_reopened_24dp
    EventAction.PUSHED, EventAction.PUSHED_NEW, EventAction.PUSHED_TO -> R.drawable.ic_event_pushed_24dp
    EventAction.UPDATED -> R.drawable.ic_event_updated_24dp
}