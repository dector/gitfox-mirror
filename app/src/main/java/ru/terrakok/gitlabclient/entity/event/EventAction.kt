package ru.terrakok.gitlabclient.entity.event

import com.google.gson.annotations.SerializedName

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17.
 */
enum class EventAction(private val jsonName: String) {
    @SerializedName("created") CREATED("created"),
    @SerializedName("updated") UPDATED("updated"),
    @SerializedName("closed") CLOSED("closed"),
    @SerializedName("reopened") REOPENED("reopened"),
    @SerializedName("pushed") PUSHED("pushed"),
    @SerializedName("commented") COMMENTED("commented"),
    @SerializedName("merged") MERGED("merged"),
    @SerializedName("joined") JOINED("joined"),
    @SerializedName("left") LEFT("left"),
    @SerializedName("destroyed") DESTROYED("destroyed"),
    @SerializedName("expired") EXPIRED("expired");

    override fun toString() = jsonName
}