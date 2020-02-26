package ru.terrakok.gitlabclient.model.data.server

import com.google.gson.Gson
import io.reactivex.Single
import okhttp3.FormBody
import okhttp3.Request
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.entity.TokenData
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.model.data.server.client.OkHttpClientFactory
import javax.inject.Inject

class UserAccountApi @Inject constructor(
    private val gson: Gson,
    okHttpClientFactory: OkHttpClientFactory
) {
    private val okHttpClient = okHttpClientFactory.create(null, false, BuildConfig.DEBUG)

    fun requestUserAccount(
        endpoint: String,
        appId: String,
        appKey: String,
        code: String,
        redirectUri: String
    ): Single<UserAccount> =
        Single
            .defer<TokenData> {
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
                try {
                    val response = okHttpClient.newCall(request).execute()
                    if (response.isSuccessful) {
                        val tokenData =
                            gson.fromJson(response.body?.charStream(), TokenData::class.java)
                        return@defer Single.just(tokenData)
                    } else {
                        val resultCode = response.code
                        if (resultCode in 400..500) throw ServerError(resultCode)
                        else throw RuntimeException("Get token data error: $resultCode")
                    }
                } catch (e: Exception) {
                    return@defer Single.error(e)
                }
            }
            .flatMap<UserAccount> { tokenData ->
                val request = Request.Builder()
                    .addHeader("Authorization", "Bearer ${tokenData.token}")
                    .url("${endpoint}api/v4/user")
                    .build()
                try {
                    val response = okHttpClient.newCall(request).execute()
                    if (response.isSuccessful) {
                        val user = gson.fromJson(response.body?.charStream(), User::class.java)
                        return@flatMap Single.just(
                            UserAccount(
                                user.id,
                                tokenData.token,
                                endpoint,
                                user.avatarUrl ?: "",
                                user.username,
                                true
                            )
                        )
                    } else {
                        val resultCode = response.code
                        if (resultCode in 400..500) throw ServerError(resultCode)
                        else throw RuntimeException("Get user data error: $resultCode")
                    }
                } catch (e: Exception) {
                    return@flatMap Single.error(e)
                }
            }

    fun requestUserAccount(
        serverPath: String,
        token: String
    ): Single<UserAccount> = Single
        .defer<UserAccount> {
            val request = Request.Builder()
                .addHeader("PRIVATE-TOKEN", token)
                .url("${serverPath}api/v4/user")
                .build()
            try {
                val response = okHttpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    val user = gson.fromJson(response.body?.charStream(), User::class.java)
                    return@defer Single.just(
                        UserAccount(
                            user.id,
                            token,
                            serverPath,
                            user.avatarUrl ?: "",
                            user.username,
                            false
                        )
                    )
                } else {
                    val code = response.code
                    if (code in 400..500) throw ServerError(code)
                    else throw RuntimeException("Custom server login error: $code")
                }
            } catch (e: Exception) {
                return@defer Single.error(e)
            }
        }
}