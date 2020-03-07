package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 30.03.17
 */
@Serializable
enum class Visibility(private val jsonName: String) {
    @SerialName("public")
    PUBLIC("public"),
    @SerialName("internal")
    INTERNAL("internal"),
    @SerialName("private")
    PRIVATE("private");

    override fun toString() = jsonName
}
