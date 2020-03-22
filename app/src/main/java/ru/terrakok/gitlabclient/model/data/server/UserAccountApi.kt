package ru.terrakok.gitlabclient.model.data.server

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import okhttp3.FormBody
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.entity.TokenData
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
            body = FormBody.Builder()
                .add("client_id", appId)
                .add("client_secret", appKey)
                .add("code", code)
                .add("redirect_uri", redirectUri)
                .add("grant_type", "authorization_code")
                .build()
        }
    }

    suspend fun requestUserAccount(
        serverPath: String,
        token: String,
        isOAuth: Boolean = false
    ): UserAccount {
        return httpClient.get("${serverPath}api/v4/user") {
            if (isOAuth) header("Authorization", "Bearer $token")
            else header("PRIVATE-TOKEN", token)
        }
    }
}
