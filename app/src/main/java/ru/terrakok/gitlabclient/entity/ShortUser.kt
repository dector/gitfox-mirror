package ru.terrakok.gitlabclient.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShortUser(
    @SerialName("id") val id: Long,
    @SerialName("state") val state: String? = null,
    @SerialName("name") val name: String,
    @SerialName("web_url") val webUrl: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("username") val username: String
)
