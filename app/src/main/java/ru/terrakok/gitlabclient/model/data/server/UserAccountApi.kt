package ru.terrakok.gitlabclient.model.data.server

import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.Parameters
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.entity.TokenData
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.model.data.server.client.HttpClientFactory

class UserAccountApi(
    httpClientFactory: HttpClientFactory
) {
    private val httpClient = httpClientFactory.create(null, BuildConfig.DEBUG)

    suspend fun requestUserAccount(
        endpoint: String,
        appId: String,
        appKey: String,
        code: String,
        redirectUri: String
    ): UserAccount {
        val tokenData = loadTokenData(endpoint, appId, appKey, code, redirectUri)
        return requestUserAccount(endpoint, tokenData.token, true)
    }

    private suspend fun loadTokenData(
        endpoint: String,
        appId: String,
        appKey: String,
        code: String,
        redirectUri: String
    ): TokenData {
        return httpClient.post<TokenData>("${endpoint}oauth/token") {
            body = FormDataContent(Parameters.build {
                append("client_id", appId)
                append("client_secret", appKey)
                append("code", code)
                append("redirect_uri", redirectUri)
                append("grant_type", "authorization_code")
            })
        }
    }

    suspend fun requestUserAccount(
        serverPath: String,
        token: String,
        isOAuth: Boolean = false
    ): UserAccount {
        val user = httpClient.get<User>("${serverPath}api/v4/user") {
            if (isOAuth) header("Authorization", "Bearer $token")
            else header("PRIVATE-TOKEN", token)
        }
        return UserAccount(
            user.id,
            token,
            serverPath,
            user.avatarUrl ?: "",
            user.username,
            isOAuth
        )
    }
}
