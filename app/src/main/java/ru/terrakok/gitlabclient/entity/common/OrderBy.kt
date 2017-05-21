package ru.terrakok.gitlabclient.entity.common

import com.google.gson.annotations.SerializedName

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 30.03.17
 */
enum class OrderBy {
    @SerializedName("id") ID,
    @SerializedName("name") NAME,
    @SerializedName("path") PATH,
    @SerializedName("created_at") CREATED_AT,
    @SerializedName("updated_at") UPDATED_AT,
    @SerializedName("last_activity_at") LAST_ACTIVITY_AT;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}