@file:UseSerializers(ZonedDateTimeDeserializer::class)
package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.model.data.server.deserializer.ZonedDateTimeDeserializer

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17.
 */
@Serializable
data class Event(
    @SerialName("project_id") val projectId: Long,
    @SerialName("action_name") val actionName: EventAction,
    @SerialName("target_id") val targetId: Long? = null,
    @SerialName("target_iid") val targetIid: Long? = null,
    @SerialName("target_type") val targetType: EventTargetType? = null,
    @SerialName("author_id") val authorId: Long,
    @SerialName("target_title") val targetTitle: String? = null,
    @SerialName("created_at") val createdAt: ZonedDateTime,
    @SerialName("author") val author: ShortUser,
    @SerialName("author_username") val authorUsername: String,
    @SerialName("push_data") val pushData: PushData? = null,
    @SerialName("note") val note: Note? = null
)

@Serializable
enum class EventAction(private val jsonName: String) {
    @SerialName("closed")
    CLOSED("closed"),
    @SerialName("commented on")
    COMMENTED_ON("commented on"),
    @SerialName("created")
    CREATED("created"),
    @SerialName("imported")
    IMPORTED("imported"),
    @SerialName("pushed to")
    PUSHED_TO("pushed to"),
    @SerialName("pushed new")
    PUSHED_NEW("pushed new"),
    @SerialName("deleted")
    DELETED("deleted"),
    @SerialName("accepted")
    ACCEPTED("accepted"),
    @SerialName("joined")
    JOINED("joined"),
    @SerialName("updated")
    UPDATED("updated"),
    @SerialName("reopened")
    REOPENED("reopened"),
    @SerialName("pushed")
    PUSHED("pushed"),
    @SerialName("commented")
    COMMENTED("commented"),
    @SerialName("merged")
    MERGED("merged"),
    @SerialName("left")
    LEFT("left"),
    @SerialName("destroyed")
    DESTROYED("destroyed"),
    @SerialName("expired")
    EXPIRED("expired"),
    @SerialName("opened")
    OPENED("opened");

    override fun toString() = jsonName
}

@Serializable
enum class EventScope(private val jsonName: String) {
    @SerialName("all")
    ALL("all");

    override fun toString() = jsonName
}

@Serializable
enum class EventTarget(private val jsonName: String) {
    @SerialName("issue")
    ISSUE("issue"),
    @SerialName("milestone")
    MILESTONE("milestone"),
    @SerialName("merge_request")
    MERGE_REQUEST("merge_request"),
    @SerialName("note")
    NOTE("note"),
    @SerialName("project")
    PROJECT("project"),
    @SerialName("snippet")
    SNIPPET("snippet"),
    @SerialName("user")
    USER("user");

    override fun toString() = jsonName
}

@Serializable
enum class EventTargetType(private val jsonName: String) {
    @SerialName("Issue")
    ISSUE("Issue"),
    @SerialName("Note")
    NOTE("Note"),
    @SerialName("DiffNote")
    DIFF_NOTE("DiffNote"),
    @SerialName("Milestone")
    MILESTONE("Milestone"),
    @SerialName("MergeRequest")
    MERGE_REQUEST("MergeRequest"),
    @SerialName("Snippet")
    SNIPPET("Snippet"),
    @SerialName("Commit")
    COMMIT("Commit"),
    @SerialName("DiscussionNote")
    DISCUSSION_NOTE("DiscussionNote");

    override fun toString() = jsonName
}
