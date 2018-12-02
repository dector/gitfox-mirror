package ru.terrakok.gitlabclient.entity.app.session

data class UserAccount(
    val userId: Long,
    val token: String,
    val serverPath: String,
    val avatarUrl: String,
    val userName: String,
    val isOAuth: Boolean
) {
    val id: String = "$userId : $serverPath"
}