package ru.terrakok.gitlabclient.entity.issue

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.entity.ShortUser
import ru.terrakok.gitlabclient.entity.TimeStats
import ru.terrakok.gitlabclient.entity.milestone.Milestone

data class Issue(
    @SerializedName("id") val id: Long,
    @SerializedName("iid") val iid: Long,
    @SerializedName("state") val state: IssueState,
    @SerializedName("description") val description: String,
    @SerializedName("author") val author: ShortUser,
    @SerializedName("milestone") val milestone: Milestone?,
    @SerializedName("project_id") val projectId: Long,
    // Assignees can be null in MergeRequest, so assume it can be null too.
    @SerializedName("assignees") val assignees: List<ShortUser>?,
    @SerializedName("updated_at") val updatedAt: ZonedDateTime?,
    @SerializedName("title") val title: String?,
    @SerializedName("created_at") val createdAt: ZonedDateTime,
    @SerializedName("labels") val labels: List<String>,
    @SerializedName("user_notes_count") val userNotesCount: Int,
    @SerializedName("due_date") val dueDate: LocalDate?,
    @SerializedName("web_url") val webUrl: String?,
    @SerializedName("confidential") val confidential: Boolean,
    @SerializedName("upvotes") val upvotes: Int,
    @SerializedName("downvotes") val downvotes: Int,
    // The closed_by attribute was introduced in GitLab 10.6.
    // This value will only be present for issues which were closed after GitLab 10.6 and
    // when the user account that closed the issue still exists.
    @SerializedName("closed_by") val closedBy: ShortUser?,
    @SerializedName("closed_at") val closedAt: ZonedDateTime?,
    // The merge_requests_count attribute was introduced in GitLab 11.9.
    @SerializedName("merge_requests_count") val relatedMergeRequestCount: Int,
    @SerializedName("time_stats") val timeStats: TimeStats?,
    @SerializedName("weight") val weight: Int?,
    @SerializedName("discussion_locked") val discussionLocked: Boolean,
    @SerializedName("assignee") val assignee: ShortUser?
)
