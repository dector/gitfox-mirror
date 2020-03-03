package ru.terrakok.gitlabclient.entity.target

import com.google.gson.annotations.SerializedName

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 18.09.17
 */
enum class TargetScope(private val jsonName: String) {
    @SerializedName("created-by-me")
    CREATED_BY_ME("created-by-me"),
    @SerializedName("assigned-to-me")
    ASSIGNED_TO_ME("assigned-to-me"),
    @SerializedName("all")
    ALL("all");

    override fun toString() = jsonName
}
