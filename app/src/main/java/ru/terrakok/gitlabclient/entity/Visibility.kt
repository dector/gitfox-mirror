package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 30.03.17
 */
enum class Visibility {
    @SerializedName("public")
    PUBLIC,
    @SerializedName("internal")
    INTERNAL,
    @SerializedName("private")
    PRIVATE;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}