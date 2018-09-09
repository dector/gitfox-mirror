package ru.terrakok.gitlabclient.entity.todo

import com.google.gson.annotations.SerializedName

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 11.09.17
 */
enum class TodoState(private val jsonName: String) {
    @SerializedName("pending")
    PENDING("pending"),
    @SerializedName("done")
    DONE("done");

    override fun toString() = jsonName
}