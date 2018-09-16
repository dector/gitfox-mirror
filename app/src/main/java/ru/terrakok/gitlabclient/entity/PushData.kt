package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 19.11.17.
 */
data class PushData(
    @SerializedName("commit_count") val commitCount: Int,
    @SerializedName("action") val action: PushDataAction,
    @SerializedName("ref_type") val refType: PushDataRefType,
    @SerializedName("commit_from") val commitFrom: String?,
    @SerializedName("commit_to") val commitTo: String?,
    @SerializedName("ref") val ref: String?,
    @SerializedName("commit_title") val commitTitle: String?
)