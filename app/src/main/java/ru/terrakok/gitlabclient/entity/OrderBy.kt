package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 30.03.17
 */
enum class OrderBy(private val jsonName: String) {
    @SerializedName("id")
    ID("id"),
    @SerializedName("name")
    NAME("name"),
    @SerializedName("path")
    PATH("path"),
    @SerializedName("created_at")
    CREATED_AT("created_at"),
    @SerializedName("updated_at")
    UPDATED_AT("updated_at"),
    @SerializedName("last_activity_at")
    LAST_ACTIVITY_AT("last_activity_at");

    override fun toString() = jsonName
}