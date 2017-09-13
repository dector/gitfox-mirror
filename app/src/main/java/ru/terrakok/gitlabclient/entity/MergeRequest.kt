package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class MergeRequest(
        @SerializedName("id") val id: Int,
        @SerializedName("iid") val iid: Int,
        @SerializedName("created_at") val createdAt: Date?,
        @SerializedName("updated_at") val updatedAt: Date?,
        @SerializedName("target_branch") val targetBranch: String?,
        @SerializedName("source_branch") val sourceBranch: String?,
        @SerializedName("project_id") val projectId: Int,
        @SerializedName("title") val title: String?,
        @SerializedName("state") val state: String?,
        @SerializedName("upvotes") val upvotes: Int,
        @SerializedName("downvotes") val downvotes: Int,
        @SerializedName("author") val author: User?,
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
        @SerializedName("labels") val labels: List<String>? = null
)
