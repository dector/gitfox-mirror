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

enum class PushDataAction(private val jsonName: String) {
    @SerializedName("pushed")
    PUSHED("pushed"),
    @SerializedName("removed")
    REMOVED("removed"),
    @SerializedName("created")
    CREATED("created");

    override fun toString() = jsonName
}

enum class PushDataRefType(private val jsonName: String) {
    @SerializedName("branch")
    BRANCH("branch"),
    @SerializedName("tag")
    TAG("tag");

    override fun toString() = jsonName
}
