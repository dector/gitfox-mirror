package ru.terrakok.gitlabclient.entity.issue

import com.google.gson.annotations.SerializedName

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 15.07.17.
 */
enum class IssueScope(private val jsonName: String) {
    @SerializedName("all")
    ALL("all"),
    @SerializedName("created-by-me")
    CREATED_BY_ME("created-by-me"),
    @SerializedName("assigned-to-me")
    ASSIGNED_BY_ME("assigned-to-me");

    override fun toString() = jsonName
}