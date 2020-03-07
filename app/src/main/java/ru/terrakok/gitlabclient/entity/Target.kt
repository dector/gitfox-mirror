package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 13.09.17
 */
abstract class Target {
    @SerializedName("id")
    private val _id: Long? = null
    @SerializedName("iid")
    private val _iid: Long? = null
    @SerializedName("project_id")
    private val _projectId: Long? = null
    @SerializedName("title")
    private val _title: String? = null
    @SerializedName("state")
    private val _state: TargetState? = null
    @SerializedName("updated_at")
    val updatedAt: ZonedDateTime? = null
    @SerializedName("created_at")
    val createdAt: ZonedDateTime? = null
    @SerializedName("labels")
    private val _labels: List<String>? = null
    @SerializedName("milestone")
    val milestone: Milestone? = null
    @SerializedName("assignees")
    private val _assignees: List<ShortUser>? = null
    @SerializedName("author")
    val _author: ShortUser? = null
    @SerializedName("assignee")
    val assignee: ShortUser? = null
    @SerializedName("user_notes_count")
    private val _userNotesCount: Int? = null
    @SerializedName("upvotes")
    private val _upVotes: Int? = null
    @SerializedName("downvotes")
    private val _downVotes: Int? = null
    @SerializedName("web_url")
    val webUrl: String? = null
    @SerializedName("subscribed")
    private val _subscribed: Boolean? = null
    @SerializedName("time_stats")
    val timeStats: TimeStats? = null

    val id get() = _id!!
    val iid get() = _iid!!
    val projectId get() = _projectId!!
    val title get() = _title!!
    val state get() = _state!!
    val labels get() = _labels!!
    val userNotesCount get() = _userNotesCount!!
    val upVotes get() = _upVotes!!
    val downVotes get() = _downVotes!!
    val subscribed get() = _subscribed!!
    val author get() = _author!!

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Target) return false

        if (id != other.id) return false
        if (iid != other.iid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + iid.hashCode()
        return result
    }

    class Issue : Target() {
        @SerializedName("due_date")
        val dueDate: LocalDate? = null
        @SerializedName("confidential")
        private val _confidential: Boolean? = null
        @SerializedName("weight")
        val weight: Long? = null
        @SerializedName("_links")
        val links: Links? = null

        val confidential get() = _confidential!!
    }

    class MergeRequest : Target() {
        @SerializedName("source_project_id")
        private val _sourceProjectId: Long? = null
        @SerializedName("target_project_id")
        private val _targetProjectId: Long? = null
        @SerializedName("target_branch")
        private val _targetBranch: String? = null
        @SerializedName("source_branch")
        private val _sourceBranch: String? = null
        @SerializedName("work_in_progress")
        val _workInProgress: Boolean? = null
        @SerializedName("merge_when_pipeline_succeeds")
        private val _mergeWhenPipelineSucceeds: Boolean? = null
        @SerializedName("merge_status")
        private val _mergeStatus: MergeRequestMergeStatus? = null
        @SerializedName("sha")
        private val _sha: String? = null
        @SerializedName("merge_commit_sha")
        val mergeCommitSha: String? = null
        @SerializedName("approvals_before_merge")
        val approvalsBeforeMerge: Int? = null
        @SerializedName("force_remove_source_branch")
        val forceRemoveSourceBranch: Boolean? = null
        @SerializedName("squash")
        private val _squash: Boolean? = null

        val sourceProjectId get() = _sourceProjectId!!
        val targetProjectId get() = _targetProjectId!!
        val targeBranch get() = _targetBranch!!
        val sourceBranch get() = _sourceBranch!!
        val workInProgress get() = _workInProgress!!
        val mergeWhenPipelineSucceeds get() = _mergeWhenPipelineSucceeds!!
        val mergeStatus get() = _mergeStatus!!
        val sha get() = _sha!!
        val squash get() = _squash!!
    }
}

enum class TargetScope(private val jsonName: String) {
    @SerializedName("created-by-me")
    CREATED_BY_ME("created-by-me"),
    @SerializedName("assigned-to-me")
    ASSIGNED_TO_ME("assigned-to-me"),
    @SerializedName("all")
    ALL("all");

    override fun toString() = jsonName
}

enum class TargetState(private val jsonName: String) {
    @SerializedName("opened")
    OPENED("opened"),
    @SerializedName("closed")
    CLOSED("closed"),
    @SerializedName("merged")
    MERGED("merged");

    override fun toString() = jsonName
}

enum class TargetType(private val jsonName: String) {
    @SerializedName("Issue")
    ISSUE("Issue"),
    @SerializedName("MergeRequest")
    MERGE_REQUEST("MergeRequest");

    override fun toString() = jsonName
}
