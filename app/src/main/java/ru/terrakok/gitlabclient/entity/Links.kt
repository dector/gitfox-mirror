package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 11.09.17
 */
data class Links(
    @SerializedName("self") val self: String?,
    @SerializedName("notes") val notes: String?,
    @SerializedName("award_emoji") val awardEmoji: String?,
    @SerializedName("project") val project: String?
)
