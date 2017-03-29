package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 30.03.17
 */
enum class Sort {
    @SerializedName("asc")
    ASC,
    @SerializedName("desc")
    DESC
}