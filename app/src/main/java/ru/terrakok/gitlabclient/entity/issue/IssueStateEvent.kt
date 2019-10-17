package ru.terrakok.gitlabclient.entity.issue

import com.google.gson.annotations.SerializedName

enum class IssueStateEvent(private val jsonName: String) {
    @SerializedName("reopen")
    REOPEN("reopen"),
    @SerializedName("close")
    CLOSE("close");

    override fun toString() = jsonName
}