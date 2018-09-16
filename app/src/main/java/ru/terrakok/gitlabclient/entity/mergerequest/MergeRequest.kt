package ru.terrakok.gitlabclient.entity.mergerequest

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime
import ru.terrakok.gitlabclient.entity.Author
import ru.terrakok.gitlabclient.entity.Milestone
import ru.terrakok.gitlabclient.entity.User

data class MergeRequest(
    @SerializedName("id") val id: Long,
    @SerializedName("iid") val iid: Long,
    @SerializedName("created_at") val createdAt: LocalDateTime,
    @SerializedName("updated_at") val updatedAt: LocalDateTime?,
    @SerializedName("target_branch") val targetBranch: String?,
    @SerializedName("source_branch") val sourceBranch: String?,
    @SerializedName("project_id") val projectId: Long,
    @SerializedName("title") val title: String?,
    @SerializedName("state") val state: MergeRequestState,
    @SerializedName("upvotes") val upvotes: Int,
    @SerializedName("downvotes") val downvotes: Int,
    @SerializedName("author") val author: Author,
    @SerializedName("assignee") val assignee: User?,
    @SerializedName("source_project_id") val sourceProjectId: Int,
    @SerializedName("target_project_id") val targetProjectId: Int,
    @SerializedName("description") val description: String?,
    @SerializedName("work_in_progress") val workInProgress: Boolean,
    @SerializedName("milestone") val milestone: Milestone?,
    @SerializedName("merge_when_pipeline_succeeds") val mergeWhenPipelineSucceeds: Boolean,
    @SerializedName("merge_status") val mergeStatus: String?,
    @SerializedName("sha") val sha: String?,
    @SerializedName("merge_commit_sha") val mergeCommitSha: String?,
    @SerializedName("user_notes_count") val userNotesCount: Int,
    @SerializedName("should_remove_source_branch") val shouldRemoveSourceBranch: Boolean,
    @SerializedName("force_remove_source_branch") val forceRemoveSourceBranch: Boolean,
    @SerializedName("web_url") val webUrl: String?,
    @SerializedName("time_stats") val timeStats: MergeRequestTimeStats?,
    @SerializedName("labels") val labels: List<String>,
    // The closed_by attribute was introduced in GitLab 10.6.
    // This value will only be present for merge requests which were closed/merged after GitLab 10.6
    // and when the user account that closed/merged the issue still exists.
    @SerializedName("closed_by") val closedBy: Author?,
    @SerializedName("closed_at") val closedAt: LocalDateTime?,
    @SerializedName("merged_by") val mergedBy: Author?,
    @SerializedName("merged_at") val mergedAt: LocalDateTime?
)
