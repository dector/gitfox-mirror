@file:UseSerializers(TimeDeserializer::class)
package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import ru.terrakok.gitlabclient.model.data.server.deserializer.TimeDeserializer

@Serializable
data class MergeRequest(
    @SerialName("id") val id: Long,
    @SerialName("iid") val iid: Long,
    @SerialName("created_at") val createdAt: Time,
    @SerialName("updated_at") val updatedAt: Time? = null,
    @SerialName("target_branch") val targetBranch: String,
    @SerialName("source_branch") val sourceBranch: String,
    @SerialName("project_id") val projectId: Long,
    @SerialName("title") val title: String? = null,
    @SerialName("state") val state: MergeRequestState,
    @SerialName("upvotes") val upvotes: Int,
    @SerialName("downvotes") val downvotes: Int,
    @SerialName("author") val author: ShortUser,
    @SerialName("assignee") val assignee: ShortUser? = null,
    @SerialName("source_project_id") val sourceProjectId: Int? = null,
    @SerialName("target_project_id") val targetProjectId: Int? = null,
    @SerialName("description") val description: String,
    @SerialName("work_in_progress") val workInProgress: Boolean,
    @SerialName("milestone") val milestone: Milestone? = null,
    @SerialName("merge_when_pipeline_succeeds") val mergeWhenPipelineSucceeds: Boolean,
    @SerialName("merge_status") val mergeStatus: MergeRequestMergeStatus,
    @SerialName("sha") val sha: String,
    @SerialName("merge_commit_sha") val mergeCommitSha: String? = null,
    @SerialName("user_notes_count") val userNotesCount: Int,
    @SerialName("should_remove_source_branch") val shouldRemoveSourceBranch: Boolean? = null,
    @SerialName("force_remove_source_branch") val forceRemoveSourceBranch: Boolean? = null,
    @SerialName("web_url") val webUrl: String? = null,
    @SerialName("labels") val labels: List<String>,
    // The closed_by attribute was introduced in GitLab 10.6.
    // This value will only be present for merge requests which were closed/merged after GitLab 10.6
    // and when the user account that closed/merged the issue still exists.
    @SerialName("closed_by") val closedBy: ShortUser? = null,
    @SerialName("closed_at") val closedAt: Time? = null,
    @SerialName("merged_by") val mergedBy: ShortUser? = null,
    @SerialName("merged_at") val mergedAt: Time? = null,
    @SerialName("changes") val diffDataList: List<DiffData>? = null,
    // It sometimes can be null.
    @SerialName("assignees") val assignees: List<ShortUser>? = null,
    @SerialName("time_stats") val timeStats: TimeStats,
    @SerialName("discussion_locked") val discussionLocked: Boolean? = null
)

@Serializable
enum class MergeRequestMergeStatus(val jsonName: String) {
    @SerialName("can_be_merged")
    CAN_BE_MERGED("can_be_merged"),
    @SerialName("cannot_be_merged")
    CANNOT_BE_MERGED("cannot_be_merged"),
    @SerialName("unchecked")
    UNCHECKED("unchecked"),
    @SerialName("checking")
    CHECKING("checking");

    override fun toString() = jsonName
}

@Serializable
enum class MergeRequestScope(val jsonName: String) {
    @SerialName("created-by-me")
    CREATED_BY_ME("created-by-me"),
    @SerialName("assigned-to-me")
    ASSIGNED_TO_ME("assigned-to-me"),
    @SerialName("all")
    ALL("all");

    override fun toString() = jsonName
}

@Serializable
enum class MergeRequestState(val jsonName: String) {
    @SerialName("opened")
    OPENED("opened"),
    @SerialName("closed")
    CLOSED("closed"),
    @SerialName("merged")
    MERGED("merged");

    override fun toString() = jsonName
}

@Serializable
enum class MergeRequestViewType(val jsonName: String) {
    @SerialName("simple")
    SIMPLE("simple");

    override fun toString() = jsonName
}
