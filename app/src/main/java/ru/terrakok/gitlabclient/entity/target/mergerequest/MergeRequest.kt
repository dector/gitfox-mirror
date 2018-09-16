package ru.terrakok.gitlabclient.entity.target.mergerequest

import com.google.gson.annotations.SerializedName
import ru.terrakok.gitlabclient.entity.target.Target

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 13.09.17
 */
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
    val approvalsBeforeMerge: Boolean? = null
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