package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 30.03.17
 */
@Serializable
enum class OrderBy(private val jsonName: String) {
    @SerialName("id")
    ID("id"),
    @SerialName("name")
    NAME("name"),
    @SerialName("path")
    PATH("path"),
    @SerialName("created_at")
    CREATED_AT("created_at"),
    @SerialName("updated_at")
    UPDATED_AT("updated_at"),
    @SerialName("last_activity_at")
    LAST_ACTIVITY_AT("last_activity_at");

    override fun toString() = jsonName
}
