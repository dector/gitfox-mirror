package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 30.03.17
 */
enum class Visibility(private val jsonName: String) {
    @SerializedName("public") PUBLIC("public"),
    @SerializedName("internal") INTERNAL("internal"),
    @SerializedName("private") PRIVATE("private");

    override fun toString() = jsonName
}