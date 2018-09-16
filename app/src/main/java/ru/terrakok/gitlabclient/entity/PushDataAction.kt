package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 19.11.17.
 */
enum class PushDataAction(private val jsonName: String) {
    @SerializedName("pushed")
    PUSHED("pushed"),
    @SerializedName("removed")
    REMOVED("removed"),
    @SerializedName("created")
    CREATED("created");

    override fun toString() = jsonName
}