package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

data class ShortUser(
    @SerializedName("id") val id: Long,
    @SerializedName("state") val state: String?,
    @SerializedName("name") val name: String,
    @SerializedName("web_url") val webUrl: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("username") val username: String
)
