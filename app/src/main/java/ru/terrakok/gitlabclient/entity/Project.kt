@file:UseSerializers(ZonedDateTimeDeserializer::class)
package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.model.data.server.deserializer.ZonedDateTimeDeserializer

@Serializable
data class Project(
    @SerialName("id") val id: Long,
    @SerialName("description") val description: String? = null,
    @SerialName("default_branch") val defaultBranch: String? = null,
    @SerialName("visibility") val visibility: Visibility? = null,
    @SerialName("ssh_url_to_repo") val sshUrlToRepo: String? = null,
    @SerialName("http_url_to_repo") val httpUrlToRepo: String? = null,
    @SerialName("web_url") val webUrl: String? = null,
    @SerialName("tag_list") val tagList: List<String>? = null,
    @SerialName("owner") val owner: Owner? = null,
    @SerialName("name") val name: String,
    @SerialName("name_with_namespace") val nameWithNamespace: String,
    @SerialName("path") val path: String,
    @SerialName("path_with_namespace") val pathWithNamespace: String,
    @SerialName("issues_enabled") val issuesEnabled: Boolean? = null,
    @SerialName("open_issues_count") val openIssuesCount: Int = 0,
    @SerialName("merge_requests_enabled") val mergeRequestsEnabled: Boolean? = null,
    @SerialName("jobs_enabled") val jobsEnabled: Boolean? = null,
    @SerialName("wiki_enabled") val wikiEnabled: Boolean? = null,
    @SerialName("snippets_enabled") val snippetsEnabled: Boolean? = null,
    @SerialName("container_registry_enabled") val containerRegistryEnabled: Boolean? = false,
    @SerialName("created_at") val createdAt: ZonedDateTime? = null,
    @SerialName("last_activity_at") val lastActivityAt: ZonedDateTime? = null,
    @SerialName("creator_id") val creatorId: Long? = null,
    @SerialName("namespace") val namespace: Namespace? = null,
    @SerialName("permissions") val permissions: Permissions? = null,
    @SerialName("archived") val archived: Boolean? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("shared_runners_enabled") val sharedRunnersEnabled: Boolean? = null,
    @SerialName("forks_count") val forksCount: Long? = null,
    @SerialName("star_count") val starCount: Long? = null,
    @SerialName("runners_token") val runnersToken: String? = null,
    @SerialName("public_jobs") val publicJobs: Boolean? = null,
    @SerialName("shared_with_groups") val sharedWithGroups: List<SharedWithGroup>? = null,
    @SerialName("only_allow_merge_if_pipeline_succeeds") val onlyAllowMergeIfPipelineSucceeds: Boolean? = null,
    @SerialName("only_allow_merge_if_all_discussions_are_resolved") val onlyAllowMergeIfAllDiscussionsAreResolved: Boolean? = null,
    @SerialName("request_access_enabled") val requestAccessEnabled: Boolean? = null,
    @SerialName("readme_url") val readmeUrl: String? = null
)
