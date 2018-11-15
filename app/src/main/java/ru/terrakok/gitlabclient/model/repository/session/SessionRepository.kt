package ru.terrakok.gitlabclient.model.repository.session

import com.google.gson.Gson
import io.reactivex.Single
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.terrakok.gitlabclient.entity.TokenData
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.model.data.server.ServerError
import ru.terrakok.gitlabclient.model.data.storage.Prefs
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultServerPath
import javax.inject.Inject

class SessionRepository @Inject constructor(
    @DefaultServerPath private val defaultServerPath: String,
    private val prefs: Prefs,
    private val gson: Gson,
    private val schedulers: SchedulersProvider
) {

    private val okHttpClient = OkHttpClient()

    fun getCurrentUserAccount(): UserAccount? {
        prefs.selectedAccount?.let { id ->
            return prefs.accounts.find { it.userId == id }
        }
        return null
    }

    fun setCurrentUserAccount(userId: Long): UserAccount? {
        val account = prefs.accounts.find { it.userId == userId }
        prefs.selectedAccount = account?.userId
        return account
    }

    fun getUserAccounts(): List<UserAccount> =
        prefs.accounts

    fun logout(userId: Long): UserAccount? {
        val newAccounts = prefs.accounts.toMutableList()
        newAccounts.removeAll { it.userId == userId }
        prefs.accounts = newAccounts

        val currentAccount = prefs.selectedAccount
        if (currentAccount == userId) {
            val newAccount = newAccounts.firstOrNull()
            prefs.selectedAccount = newAccount?.userId
            return newAccount
        } else {
            return newAccounts.find { it.userId == currentAccount }
        }
    }

    fun login(
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
                    .url("$defaultServerPath/oauth/token")
                    .post(body)
                    .build()
                try {
                    val response = okHttpClient.newCall(request).execute()
                    if (response.isSuccessful) {
                        val tokenData = gson.fromJson(response.body()?.charStream(), TokenData::class.java)
                        return@defer Single.just(tokenData)
                    } else {
                        val resultCode = response.code()
                        if (resultCode in 400..500) throw ServerError(resultCode)
                        else throw UnknownError("Get token data error: $resultCode")
                    }
                } catch (e: Exception) {
                    return@defer Single.error(e)
                }
            }
            .flatMap<UserAccount> { tokenData ->
                val request = Request.Builder()
                    .addHeader("Authorization", "Bearer ${tokenData.token}")
                    .url("$defaultServerPath/user")
                    .build()
                try {
                    val response = okHttpClient.newCall(request).execute()
                    if (response.isSuccessful) {
                        val user = gson.fromJson(response.body()?.charStream(), User::class.java)
                        return@flatMap Single.just(
                            UserAccount(
                                user.id,
                                tokenData.token,
                                defaultServerPath,
                                user.avatarUrl ?: "",
                                user.username,
                                true
                            )
                        )
                    } else {
                        val resultCode = response.code()
                        if (resultCode in 400..500) throw ServerError(resultCode)
                        else throw UnknownError("Get user data error: $resultCode")
                    }
                } catch (e: Exception) {
                    return@flatMap Single.error(e)
                }
            }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSuccess { saveNewAccount(it) }

    fun login(
        token: String,
        serverPath: String
    ): Single<UserAccount> =
        Single
            .defer<UserAccount> {
                val request = Request.Builder()
                    .addHeader("PRIVATE-TOKEN", token)
                    .url("$serverPath/user")
                    .build()
                try {
                    val response = okHttpClient.newCall(request).execute()
                    if (response.isSuccessful) {
                        val user = gson.fromJson(response.body()?.charStream(), User::class.java)
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
                        val code = response.code()
                        if (code in 400..500) throw ServerError(code)
                        else throw UnknownError("Custom server login error: $code")
                    }
                } catch (e: Exception) {
                    return@defer Single.error(e)
                }
            }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSuccess { saveNewAccount(it) }

    private fun saveNewAccount(userAccount: UserAccount) {
        val newAccounts = prefs.accounts.toMutableList()
        newAccounts.removeAll { it.userId == userAccount.userId }
        newAccounts.add(0, userAccount)
        prefs.accounts = newAccounts
    }
}
