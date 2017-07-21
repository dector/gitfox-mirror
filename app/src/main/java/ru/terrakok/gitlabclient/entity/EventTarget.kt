package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17.
 */
enum class EventTarget {
    @SerializedName("issue") ISSUE,
    @SerializedName("milestone") MILESTONE,
    @SerializedName("merge_request") MERGE_REQUEST,
    @SerializedName("note") NOTE,
    @SerializedName("project") PROJECT,
    @SerializedName("snippet") SNIPPET,
    @SerializedName("user") USER;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}