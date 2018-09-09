package ru.terrakok.gitlabclient.entity.event

import com.google.gson.annotations.SerializedName

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17.
 */
enum class EventTargetType(private val jsonName: String) {
    @SerializedName("Issue")
    ISSUE("Issue"),
    @SerializedName("Note")
    NOTE("Note"),
    @SerializedName("DiffNote")
    DIFF_NOTE("DiffNote"),
    @SerializedName("Milestone")
    MILESTONE("Milestone"),
    @SerializedName("MergeRequest")
    MERGE_REQUEST("MergeRequest"),
    @SerializedName("Snippet")
    SNIPPET("Snippet");

    override fun toString() = jsonName
}