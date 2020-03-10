package ru.terrakok.gitlabclient.model.data.server

import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.serialization.json.Json
import okhttp3.*
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.entity.TokenData
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.model.data.server.client.OkHttpClientFactory
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class UserAccountApi @Inject constructor(
    private val json: Json,
    okHttpClientFactory: OkHttpClientFactory
) {
    private val okHttpClient = okHttpClientFactory.create(null, false, BuildConfig.DEBUG)

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
    ): TokenData = suspendCancellableCoroutine { continuation ->
        val body = FormBody.Builder()
            .add("client_id", appId)
            .add("client_secret", appKey)
            .add("code", code)
            .add("redirect_uri", redirectUri)
            .add("grant_type", "authorization_code")
            .build()
        val request = Request.Builder()
            .url("${endpoint}oauth/token")
            .post(body)
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    continuation.resume(
                        json.parse(
                            TokenData.serializer(),
                            response.body!!.string()
                        )
                    )
                } else {
                    val resultCode = response.code
                    continuation.resumeWithException(
                        if (resultCode in 400..500) ServerError(resultCode)
                        else RuntimeException("Get token data error: $resultCode")
                    )
                }
            }
        })
    }

    suspend fun requestUserAccount(
        serverPath: String,
        token: String,
        isOAuth: Boolean = false
    ): UserAccount = suspendCancellableCoroutine { continuation ->
        val request = Request.Builder().apply {
            if (isOAuth) addHeader("Authorization", "Bearer $token")
            else addHeader("PRIVATE-TOKEN", token)
            url("${serverPath}api/v4/user")
        }.build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val user = json.parse(User.serializer(), response.body!!.string())
                    continuation.resume(
                        UserAccount(
                            user.id,
                            token,
                            serverPath,
                            user.avatarUrl ?: "",
                            user.username,
                            true
                        )
                    )
                } else {
                    val resultCode = response.code
                    continuation.resumeWithException(
                        if (resultCode in 400..500) ServerError(resultCode)
                        else RuntimeException("Get token data error: $resultCode")
                    )
                }
            }
        })
    }
}
