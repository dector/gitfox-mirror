package ru.terrakok.gitlabclient.entity.event

import com.google.gson.annotations.SerializedName

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17.
 */
enum class EventTarget(private val jsonName: String) {
    @SerializedName("issue") ISSUE("issue"),
    @SerializedName("milestone") MILESTONE("milestone"),
    @SerializedName("merge_request") MERGE_REQUEST("merge_request"),
    @SerializedName("note") NOTE("note"),
    @SerializedName("project") PROJECT("project"),
    @SerializedName("snippet") SNIPPET("snippet"),
    @SerializedName("user") USER("user");

    override fun toString() = jsonName
}