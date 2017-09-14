package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 30.03.17
 */
enum class Sort(private val jsonName: String) {
    @SerializedName("asc") ASC("asc"),
    @SerializedName("desc") DESC("desc");

    override fun toString() = jsonName
}