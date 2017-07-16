package ru.terrakok.gitlabclient.entity.common

import com.google.gson.annotations.SerializedName

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 15.07.17.
 */
enum class IssueState {
    @SerializedName("opened") OPENED,
    @SerializedName("closed") CLOSED;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}