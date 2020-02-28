package ru.terrakok.gitlabclient.entity.event

import com.google.gson.annotations.SerializedName

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17.
 */
enum class EventAction(private val jsonName: String) {
    @SerializedName("closed")
    CLOSED("closed"),
    @SerializedName("commented on")
    COMMENTED_ON("commented on"),
    @SerializedName("created")
    CREATED("created"),
    @SerializedName("imported")
    IMPORTED("imported"),
    @SerializedName("pushed to")
    PUSHED_TO("pushed to"),
    @SerializedName("pushed new")
    PUSHED_NEW("pushed new"),
    @SerializedName("deleted")
    DELETED("deleted"),
    @SerializedName("accepted")
    ACCEPTED("accepted"),
    @SerializedName("joined")
    JOINED("joined"),
    @SerializedName("updated")
    UPDATED("updated"),
    @SerializedName("reopened")
    REOPENED("reopened"),
    @SerializedName("pushed")
    PUSHED("pushed"),
    @SerializedName("commented")
    COMMENTED("commented"),
    @SerializedName("merged")
    MERGED("merged"),
    @SerializedName("left")
    LEFT("left"),
    @SerializedName("destroyed")
    DESTROYED("destroyed"),
    @SerializedName("expired")
    EXPIRED("expired"),
    @SerializedName("opened")
    OPENED("opened");

    override fun toString() = jsonName
}
