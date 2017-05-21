package ru.terrakok.gitlabclient.entity.common

import com.google.gson.annotations.SerializedName
import java.util.*

data class Project(
        @SerializedName("id") val id: Long,
        @SerializedName("description") val description: String?,
        @SerializedName("default_branch") val defaultBranch: String,
        @SerializedName("visibility") val visibility: Visibility,
        @SerializedName("ssh_url_to_repo") val sshUrlToRepo: String?,
        @SerializedName("http_url_to_repo") val httpUrlToRepo: String?,
        @SerializedName("web_url") val webUrl: String?,
        @SerializedName("tag_list") val tagList: List<String>?,
        @SerializedName("owner") val owner: Owner?,
        @SerializedName("name") val name: String?,
        @SerializedName("name_with_namespace") val nameWithNamespace: String?,
        @SerializedName("path") val path: String,
        @SerializedName("path_with_namespace") val pathWithNamespace: String?,
        @SerializedName("issues_enabled") val issuesEnabled: Boolean,
        @SerializedName("open_issues_count") val openIssuesCount: Long,
        @SerializedName("merge_requests_enabled") val mergeRequestsEnabled: Boolean,
        @SerializedName("jobs_enabled") val jobsEnabled: Boolean,
        @SerializedName("wiki_enabled") val wikiEnabled: Boolean,
        @SerializedName("snippets_enabled") val snippetsEnabled: Boolean,
        @SerializedName("container_registry_enabled") val containerRegistryEnabled: Boolean,
        @SerializedName("created_at") val createdAt: Date?,
        @SerializedName("last_activity_at") val lastActivityAt: Date?,
        @SerializedName("creator_id") val creatorId: Long,
        @SerializedName("namespace") val namespace: Namespace?,
        @SerializedName("permissions") val permissions: Permissions?,
        @SerializedName("archived") val archived: Boolean,
        @SerializedName("avatar_url") val avatarUrl: String?,
        @SerializedName("shared_runners_enabled") val sharedRunnersEnabled: Boolean,
        @SerializedName("forks_count") val forksCount: Long,
        @SerializedName("star_count") val starCount: Long,
        @SerializedName("runners_token") val runnersToken: String?,
        @SerializedName("public_jobs") val publicJobs: Boolean,
        @SerializedName("shared_with_groups") val sharedWithGroups: List<SharedWithGroup>?,
        @SerializedName("only_allow_merge_if_pipeline_succeeds") val onlyAllowMergeIfPipelineSucceeds: Boolean,
        @SerializedName("only_allow_merge_if_all_discussions_are_resolved") val onlyAllowMergeIfAllDiscussionsAreResolved: Boolean,
        @SerializedName("request_access_enabled") val requestAccessEnabled: Boolean
)
