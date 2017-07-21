package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
data class TokenData(
        @SerializedName("access_token") val token: String,
        @SerializedName("token_type") val type: String,
        @SerializedName("scope") val scope: String,
        @SerializedName("created_at") val createdAt: Long,
        @SerializedName("refresh_token") val refreshToken: String
)