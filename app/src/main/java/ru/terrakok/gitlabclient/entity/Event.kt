package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName
import org.threeten.bp.ZonedDateTime

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17.
 */
data class Event(
    @SerializedName("project_id") val projectId: Long,
    @SerializedName("action_name") val actionName: EventAction,
    @SerializedName("target_id") val targetId: Long?,
    @SerializedName("target_iid") val targetIid: Long?,
    @SerializedName("target_type") val targetType: EventTargetType?,
    @SerializedName("author_id") val authorId: Long,
    @SerializedName("target_title") val targetTitle: String?,
    @SerializedName("created_at") val createdAt: ZonedDateTime,
    @SerializedName("author") val author: ShortUser,
    @SerializedName("author_username") val authorUsername: String,
    @SerializedName("push_data") val pushData: PushData?,
    @SerializedName("note") val note: Note?
)

enum class EventAction(private val jsonName: String) {
    @SerializedName("closed")
    CLOSED("closed"),
    @SerializedName("commented on")
    COMMENTED_ON("commented on"),
    @SerializedName("created")
    CREATED("created"),
    @SerializedName("imported")
    IMPORTED("imported"),
    @SerializedName("pushed to")
    PUSHED_TO("pushed to"),
    @SerializedName("pushed new")
    PUSHED_NEW("pushed new"),
    @SerializedName("deleted")
    DELETED("deleted"),
    @SerializedName("accepted")
    ACCEPTED("accepted"),
    @SerializedName("joined")
    JOINED("joined"),
    @SerializedName("updated")
    UPDATED("updated"),
    @SerializedName("reopened")
    REOPENED("reopened"),
    @SerializedName("pushed")
    PUSHED("pushed"),
    @SerializedName("commented")
    COMMENTED("commented"),
    @SerializedName("merged")
    MERGED("merged"),
    @SerializedName("left")
    LEFT("left"),
    @SerializedName("destroyed")
    DESTROYED("destroyed"),
    @SerializedName("expired")
    EXPIRED("expired"),
    @SerializedName("opened")
    OPENED("opened");

    override fun toString() = jsonName
}

enum class EventScope(private val jsonName: String) {
    @SerializedName("all")
    ALL("all");

    override fun toString() = jsonName
}

enum class EventTarget(private val jsonName: String) {
    @SerializedName("issue")
    ISSUE("issue"),
    @SerializedName("milestone")
    MILESTONE("milestone"),
    @SerializedName("merge_request")
    MERGE_REQUEST("merge_request"),
    @SerializedName("note")
    NOTE("note"),
    @SerializedName("project")
    PROJECT("project"),
    @SerializedName("snippet")
    SNIPPET("snippet"),
    @SerializedName("user")
    USER("user");

    override fun toString() = jsonName
}

enum class EventTargetType(private val jsonName: String) {
    @SerializedName("Issue")
    ISSUE("Issue"),
    @SerializedName("Note")
    NOTE("Note"),
    @SerializedName("DiffNote")
    DIFF_NOTE("DiffNote"),
    @SerializedName("Milestone")
    MILESTONE("Milestone"),
    @SerializedName("MergeRequest")
    MERGE_REQUEST("MergeRequest"),
    @SerializedName("Snippet")
    SNIPPET("Snippet");

    override fun toString() = jsonName
}
