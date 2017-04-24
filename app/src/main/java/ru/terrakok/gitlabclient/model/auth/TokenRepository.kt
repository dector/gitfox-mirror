package ru.terrakok.gitlabclient.model.auth

import ru.terrakok.gitlabclient.model.server.GitlabApi

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
class TokenRepository(private val api: GitlabApi) {
    fun getToken(
            appId: String,
            appKey: String,
            code: String,
            redirectUri: String) = api.auth(appId, appKey, code, redirectUri)
}