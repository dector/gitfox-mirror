@file:UseSerializers(
    ZonedDateTimeDeserializer::class,
    LocalDateDeserializer::class
)
package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.model.data.server.deserializer.LocalDateDeserializer
import ru.terrakok.gitlabclient.model.data.server.deserializer.ZonedDateTimeDeserializer

sealed class Target {
    abstract val id: Long
    abstract val iid: Long
    abstract val projectId: Long
    abstract val title: String
    abstract val state: TargetState
    abstract val updatedAt: ZonedDateTime?
    abstract val createdAt: ZonedDateTime?
    abstract val labels: List<String>
    abstract val milestone: Milestone?
    abstract val assignees: List<ShortUser>?
    abstract val author: ShortUser
    abstract val assignee: ShortUser?
    abstract val userNotesCount: Int?
    abstract val upVotes: Int?
    abstract val downVotes: Int?
    abstract val webUrl: String?
    abstract val subscribed: Boolean?
    abstract val timeStats: TimeStats?

    @Serializable
    data class Issue(
        @SerialName("id") override val id: Long,
        @SerialName("iid") override val iid: Long,
        @SerialName("project_id") override val projectId: Long,
        @SerialName("title") override val title: String,
        @SerialName("state") override val state: TargetState,
        @SerialName("updated_at") override val updatedAt: ZonedDateTime? = null,
        @SerialName("created_at") override val createdAt: ZonedDateTime? = null,
        @SerialName("labels") override val labels: List<String>,
        @SerialName("milestone") override val milestone: Milestone? = null,
        @SerialName("assignees") override val assignees: List<ShortUser>? = null,
        @SerialName("author") override val author: ShortUser,
        @SerialName("assignee") override val assignee: ShortUser? = null,
        @SerialName("user_notes_count") override val userNotesCount: Int? = null,
        @SerialName("upvotes") override val upVotes: Int? = null,
        @SerialName("downvotes") override val downVotes: Int? = null,
        @SerialName("web_url") override val webUrl: String? = null,
        @SerialName("subscribed") override val subscribed: Boolean? = null,
        @SerialName("time_stats") override val timeStats: TimeStats? = null,
        @SerialName("due_date") val dueDate: LocalDate? = null,
        @SerialName("confidential") val confidential: Boolean,
        @SerialName("weight") val weight: Long? = null,
        @SerialName("_links") val links: Links? = null
    ) : Target()

    @Serializable
    data class MergeRequest(
        @SerialName("id") override val id: Long,
        @SerialName("iid") override val iid: Long,
        @SerialName("project_id") override val projectId: Long,
        @SerialName("title") override val title: String,
        @SerialName("state") override val state: TargetState,
        @SerialName("updated_at") override val updatedAt: ZonedDateTime? = null,
        @SerialName("created_at") override val createdAt: ZonedDateTime? = null,
        @SerialName("labels") override val labels: List<String>,
        @SerialName("milestone") override val milestone: Milestone? = null,
        @SerialName("assignees") override val assignees: List<ShortUser>? = null,
        @SerialName("author") override val author: ShortUser,
        @SerialName("assignee") override val assignee: ShortUser? = null,
        @SerialName("user_notes_count") override val userNotesCount: Int? = null,
        @SerialName("upvotes") override val upVotes: Int? = null,
        @SerialName("downvotes") override val downVotes: Int? = null,
        @SerialName("web_url") override val webUrl: String? = null,
        @SerialName("subscribed") override val subscribed: Boolean? = null,
        @SerialName("time_stats") override val timeStats: TimeStats? = null,
        @SerialName("source_project_id") val sourceProjectId: Long,
        @SerialName("target_project_id") val targetProjectId: Long,
        @SerialName("target_branch") val targetBranch: String,
        @SerialName("source_branch") val sourceBranch: String,
        @SerialName("work_in_progress") val workInProgress: Boolean,
        @SerialName("merge_when_pipeline_succeeds") val mergeWhenPipelineSucceeds: Boolean,
        @SerialName("merge_status") val mergeStatus: MergeRequestMergeStatus,
        @SerialName("sha") val sha: String? = null,
        @SerialName("merge_commit_sha") val mergeCommitSha: String? = null,
        @SerialName("approvals_before_merge") val approvalsBeforeMerge: Int? = null,
        @SerialName("force_remove_source_branch") val forceRemoveSourceBranch: Boolean? = null,
        @SerialName("squash") val squash: Boolean? = null
    ) : Target()
}

@Serializable
enum class TargetScope(private val jsonName: String) {
    @SerialName("created-by-me")
    CREATED_BY_ME("created-by-me"),
    @SerialName("assigned-to-me")
    ASSIGNED_TO_ME("assigned-to-me"),
    @SerialName("all")
    ALL("all");

    override fun toString() = jsonName
}

@Serializable
enum class TargetState(private val jsonName: String) {
    @SerialName("opened")
    OPENED("opened"),
    @SerialName("closed")
    CLOSED("closed"),
    @SerialName("merged")
    MERGED("merged");

    override fun toString() = jsonName
}

@Serializable
enum class TargetType(private val jsonName: String) {
    @SerialName("Issue")
    ISSUE("Issue"),
    @SerialName("MergeRequest")
    MERGE_REQUEST("MergeRequest");

    override fun toString() = jsonName
}
