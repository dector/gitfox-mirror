package ru.terrakok.gitlabclient.entity.app.session

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserAccount(
    @SerialName("userId") val userId: Long,
    @SerialName("token") val token: String,
    @SerialName("serverPath") val serverPath: String,
    @SerialName("avatarUrl") val avatarUrl: String,
    @SerialName("userName") val userName: String,
    @SerialName("isOAuth") val isOAuth: Boolean
) {
    val id: String = "$userId : $serverPath"
}
