package ru.terrakok.gitlabclient.entity.target

import com.google.gson.annotations.SerializedName

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 18.09.17
 */
enum class TargetState(private val jsonName: String) {
    @SerializedName("opened") OPENED("opened"),
    @SerializedName("closed") CLOSED("closed"),
    @SerializedName("merged") MERGED("merged");

    override fun toString() = jsonName
}