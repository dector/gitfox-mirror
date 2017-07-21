package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17.
 */
enum class EventAction {
    @SerializedName("created") CREATED,
    @SerializedName("updated") UPDATED,
    @SerializedName("closed") CLOSED,
    @SerializedName("reopened") REOPENED,
    @SerializedName("pushed") PUSHED,
    @SerializedName("commented") COMMENTED,
    @SerializedName("merged") MERGED,
    @SerializedName("joined") JOINED,
    @SerializedName("left") LEFT,
    @SerializedName("destroyed") DESTROYED,
    @SerializedName("expired") EXPIRED;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}