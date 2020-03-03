package ru.terrakok.gitlabclient.entity.target

import com.google.gson.annotations.SerializedName

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 11.09.17
 */
enum class TargetType(private val jsonName: String) {
    @SerializedName("Issue")
    ISSUE("Issue"),
    @SerializedName("MergeRequest")
    MERGE_REQUEST("MergeRequest");

    override fun toString() = jsonName
}
