package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Member(
    @SerialName("id") val id: Long,
    @SerialName("username") val username: String,
    @SerialName("name") val name: String,
    @SerialName("state") val state: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("web_url") val webUrl: String? = null,
    @SerialName("expires_at") val expiresDate: String? = null,
    @SerialName("access_level") val accessLevel: Long
)
