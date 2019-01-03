package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * @author Maxim Myalkin (MaxMyalkin) on 29.10.2018.
 */
data class Label(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    // The color of the label given in 6-digit hex notation with leading ‘#’ sign (e.g. #FFAABB)
    // or one of the CSS color names. So according to CSS color names it can be named differently on Android.
    @SerializedName("color") val color: String,
    @SerializedName("description") val description: String?,
    @SerializedName("open_issues_count") val openIssuesCount: Int,
    @SerializedName("closed_issues_count") val closedIssuesCount: Int,
    @SerializedName("open_merge_requests_count") val openMergeRequestsCount: Int,
    @SerializedName("subscribed") val subscribed: Boolean,
    @SerializedName("priority") val priority: Int?
)