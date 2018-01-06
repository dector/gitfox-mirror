package ru.terrakok.gitlabclient.entity.issue

import com.google.gson.annotations.SerializedName

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 15.07.17.
 */
enum class IssueState(private val jsonName: String) {
    @SerializedName("opened") OPENED("opened"),
    @SerializedName("closed") CLOSED("closed");

    override fun toString() = jsonName
}
