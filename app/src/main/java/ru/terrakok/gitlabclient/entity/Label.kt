package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

data class Label(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("color") val color: String,
    @SerializedName("description") val description: String?,
    @SerializedName("open_issues_count") val openIssuesCount: Int,
    @SerializedName("closed_issues_count") val closedIssuesCount: Int,
    @SerializedName("open_merge_requests_count") val openMergeRequestsCount: Int,
    @SerializedName("subscribed") val subscribed: Boolean,
    @SerializedName("priority") val priority: Int?
)