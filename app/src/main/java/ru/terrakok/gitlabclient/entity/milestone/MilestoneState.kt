package ru.terrakok.gitlabclient.entity.milestone

import com.google.gson.annotations.SerializedName

/**
 * @author Valentin Logvinovitch (@glvvl) on 22.11.18.
 */
enum class MilestoneState(private val jsonName: String) {
    @SerializedName("active")
    ACTIVE("active"),
    @SerializedName("closed")
    CLOSED("closed");

    override fun toString() = jsonName
}
