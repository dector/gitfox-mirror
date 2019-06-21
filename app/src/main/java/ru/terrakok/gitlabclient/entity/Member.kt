package ru.terrakok.gitlabclient.entity

import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("id") val id: Long,
    @SerializedName("username") val username: String,
    @SerializedName("name") val name: String,
    @SerializedName("state") val state: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("web_url") val webUrl: String?,
    @SerializedName("expires_at") val expiresDate: String?,
    @SerializedName("access_level") val accessLevel: Long
)