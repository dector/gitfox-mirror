package ru.terrakok.gitlabclient.entity.common

import com.google.gson.annotations.SerializedName

data class Author(
        @SerializedName("id") val id: Long,
        @SerializedName("state") val state: String?,
        @SerializedName("web_url") val webUrl: String?,
        @SerializedName("name") val name: String?,
        @SerializedName("avatar_url") val avatarUrl: String?,
        @SerializedName("username") val username: String?
)
