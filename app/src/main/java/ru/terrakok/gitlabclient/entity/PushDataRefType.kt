package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 05.12.17.
 */
enum class PushDataRefType(private val jsonName: String) {
    @SerializedName("branch")
    BRANCH("branch"),
    @SerializedName("tag")
    TAG("tag");

    override fun toString() = jsonName
}