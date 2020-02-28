package ru.terrakok.gitlabclient.entity.app.session

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 25.09.17.
 */
data class OAuthParams(
    val endpoint: String,
    val appId: String,
    val appKey: String,
    val redirectUrl: String
)
