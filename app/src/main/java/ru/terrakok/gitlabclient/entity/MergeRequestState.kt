package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

enum class MergeRequestState(val jsonName: String) {
    @SerializedName("opened") OPENED("opened"),
    @SerializedName("closed") CLOSED("closed"),
    @SerializedName("merged") MERGED("merged");

    override fun toString() = jsonName
}
