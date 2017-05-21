package ru.terrakok.gitlabclient.entity.common

import com.google.gson.annotations.SerializedName

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
data class TokenData(
        @SerializedName("access_token") val token: String,
        @SerializedName("token_type") val type: String,
        @SerializedName("expires_in") val expires: Long,
        @SerializedName("refresh_token") val refreshToken: String
)