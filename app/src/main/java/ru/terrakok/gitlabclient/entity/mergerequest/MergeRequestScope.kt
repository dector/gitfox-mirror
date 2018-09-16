package ru.terrakok.gitlabclient.entity.mergerequest

import com.google.gson.annotations.SerializedName

enum class MergeRequestScope(val jsonName: String) {
    @SerializedName("created-by-me")
    CREATED_BY_ME("created-by-me"),
    @SerializedName("assigned-to-me")
    ASSIGNED_TO_ME("assigned-to-me"),
    @SerializedName("all")
    ALL("all");

    override fun toString() = jsonName
}