package ru.terrakok.gitlabclient.model.interactor.auth

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 25.09.17.
 */
data class OAuthParams(
        val appId: String,
        val appKey: String,
        val redirectUrl: String
)