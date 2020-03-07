@file:UseSerializers(
    LocalDateDeserializer::class,
    ZonedDateTimeDeserializer::class
)
package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.model.data.server.deserializer.LocalDateDeserializer
import ru.terrakok.gitlabclient.model.data.server.deserializer.ZonedDateTimeDeserializer

@Serializable
data class Issue(
    @SerialName("id") val id: Long,
    @SerialName("iid") val iid: Long,
    @SerialName("state") val state: IssueState,
    @SerialName("description") val description: String,
    @SerialName("author") val author: ShortUser,
    @SerialName("milestone") val milestone: Milestone? = null,
    @SerialName("project_id") val projectId: Long,
    // Assignees can be null in MergeRequest, so assume it can be null too.
    @SerialName("assignees") val assignees: List<ShortUser>? = null,
    @SerialName("updated_at") val updatedAt: ZonedDateTime? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("created_at") val createdAt: ZonedDateTime,
    @SerialName("labels") val labels: List<String>,
    @SerialName("user_notes_count") val userNotesCount: Int,
    @SerialName("due_date") val dueDate: LocalDate? = null,
    @SerialName("web_url") val webUrl: String? = null,
    @SerialName("confidential") val confidential: Boolean,
    @SerialName("upvotes") val upvotes: Int,
    @SerialName("downvotes") val downvotes: Int,
    // The closed_by attribute was introduced in GitLab 10.6.
    // This value will only be present for issues which were closed after GitLab 10.6 and
    // when the user account that closed the issue still exists.
    @SerialName("closed_by") val closedBy: ShortUser? = null,
    @SerialName("closed_at") val closedAt: ZonedDateTime? = null,
    // The merge_requests_count attribute was introduced in GitLab 11.9.
    @SerialName("merge_requests_count") val relatedMergeRequestCount: Int,
    @SerialName("time_stats") val timeStats: TimeStats? = null,
    @SerialName("weight") val weight: Int? = null,
    @SerialName("discussion_locked") val discussionLocked: Boolean? = null,
    @SerialName("assignee") val assignee: ShortUser? = null
)

@Serializable
enum class IssueScope(private val jsonName: String) {
    @SerialName("all")
    ALL("all"),
    @SerialName("created-by-me")
    CREATED_BY_ME("created-by-me"),
    @SerialName("assigned-to-me")
    ASSIGNED_BY_ME("assigned-to-me");

    override fun toString() = jsonName
}

@Serializable
enum class IssueState(private val jsonName: String) {
    @SerialName("opened")
    OPENED("opened"),
    @SerialName("closed")
    CLOSED("closed");

    override fun toString() = jsonName
}

@Serializable
enum class IssueStateEvent(private val jsonName: String) {
    @SerialName("reopen")
    REOPEN("reopen"),
    @SerialName("close")
    CLOSE("close");

    override fun toString() = jsonName
}
