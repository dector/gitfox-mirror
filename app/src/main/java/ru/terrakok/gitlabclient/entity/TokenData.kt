package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.03.17.
 */
@Serializable
data class TokenData(
    @SerialName("access_token") val token: String,
    @SerialName("token_type") val type: String,
    @SerialName("scope") val scope: String,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("refresh_token") val refreshToken: String
)
