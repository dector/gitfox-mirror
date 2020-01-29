package ru.terrakok.gitlabclient.entity.event

import com.google.gson.annotations.SerializedName

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17.
 */
enum class EventScope(private val jsonName: String) {
    @SerializedName("all")
    ALL("all");

    override fun toString() = jsonName
}