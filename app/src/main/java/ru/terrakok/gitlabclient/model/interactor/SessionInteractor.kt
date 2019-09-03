package ru.terrakok.gitlabclient.model.interactor

import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.di.DefaultServerPath
import ru.terrakok.gitlabclient.di.ServerPath
import ru.terrakok.gitlabclient.di.module.ServerModule
import ru.terrakok.gitlabclient.entity.TokenData
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.entity.app.session.OAuthParams
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.model.data.cache.ProjectCache
import ru.terrakok.gitlabclient.model.data.server.ServerError
import ru.terrakok.gitlabclient.model.data.server.Tls12SocketFactory.Companion.enableTls12
import ru.terrakok.gitlabclient.model.data.storage.Prefs
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import toothpick.Toothpick
import java.net.URI
import java.util.*
import javax.inject.Inject

class SessionInteractor @Inject constructor(
    @ServerPath serverPath: String,
    @DefaultServerPath private val defaultServerPath: String,
    private val prefs: Prefs,
    private val oauthParams: OAuthParams,
    private val gson: Gson,
    private val projectCache: ProjectCache,
    private val schedulers: SchedulersProvider
) {
    private val hash = UUID.randomUUID().toString()
    private val okHttpClient = OkHttpClient.Builder()
        .enableTls12()
        .build()

    val oauthUrl = "${serverPath}oauth/authorize?client_id=${oauthParams.appId}" +
            "&redirect_uri=${oauthParams.redirectUrl}&response_type=code&state=$hash"

    fun checkOAuthRedirect(url: String) = url.indexOf(oauthParams.redirectUrl) == 0

    fun getCurrentUserAccount(): UserAccount? =
        prefs.getCurrentUserAccount()

    fun setCurrentUserAccount(accountId: String): UserAccount? {
        val newAccount = setUserAccount(accountId)
        switchAccount(newAccount)
        return newAccount
    }

    private fun setUserAccount(accountId: String): UserAccount? {
        val account = prefs.accounts.find { it.id == accountId }
        prefs.selectedAccount = account?.id
        return account
    }

    fun getUserAccounts(): List<UserAccount> =
        prefs.accounts

    // Return hasOtherAccount
    fun logout(): Boolean {
        val currentAccount = getCurrentUserAccount()
        if (currentAccount != null) {
            return logoutFromAccount(currentAccount.id)
        } else {
            return false
        }
    }

    // Return hasOtherAccount
    fun logoutFromAccount(accountId: String): Boolean {
        projectCache.clear()
        val newAccount = logout(accountId)
        switchAccount(newAccount)
        return newAccount != null
    }

    private fun logout(accountId: String): UserAccount? {
        val newAccounts = prefs.accounts.toMutableList()
        newAccounts.removeAll { it.id == accountId }
        prefs.accounts = newAccounts

        val currentAccount = prefs.selectedAccount
        if (currentAccount == accountId) {
            val newAccount = newAccounts.firstOrNull()
            prefs.selectedAccount = newAccount?.id
            return newAccount
        } else {
            return newAccounts.find { it.id == currentAccount }
        }
    }

    fun login(oauthRedirect: String): Completable =
        Completable.defer {
            if (oauthRedirect.contains(hash)) {
                login(
                    oauthParams.appId,
                    oauthParams.appKey,
                    getQueryParameterFromUri(oauthRedirect, PARAMETER_CODE),
                    oauthParams.redirectUrl
                )
                    .doOnSuccess { switchAccount(it) }
                    .ignoreElement()
            } else {
                Completable.error(RuntimeException("Not valid oauth hash!"))
            }
        }

    private fun login(
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
                    .url("${defaultServerPath}oauth/token")
                    .post(body)
                    .build()
                try {
                    val response = okHttpClient.newCall(request).execute()
                    if (response.isSuccessful) {
                        val tokenData =
                            gson.fromJson(response.body()?.charStream(), TokenData::class.java)
                        return@defer Single.just(tokenData)
                    } else {
                        val resultCode = response.code()
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
                    .url("${defaultServerPath}api/v4/user")
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
                        else throw RuntimeException("Get user data error: $resultCode")
                    }
                } catch (e: Exception) {
                    return@flatMap Single.error(e)
                }
            }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSuccess { saveNewAccount(it) }

    fun loginOnCustomServer(
        token: String,
        serverPath: String
    ): Completable =
        Single
            .defer<UserAccount> {
                val request = Request.Builder()
                    .addHeader("PRIVATE-TOKEN", token)
                    .url("${serverPath}api/v4/user")
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
                        else throw RuntimeException("Custom server login error: $code")
                    }
                } catch (e: Exception) {
                    return@defer Single.error(e)
                }
            }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSuccess {
                saveNewAccount(it)
                switchAccount(it)
            }
            .ignoreElement()

    private fun saveNewAccount(userAccount: UserAccount) {
        val newAccounts = prefs.accounts.toMutableList()
        newAccounts.removeAll { it.id == userAccount.id }
        newAccounts.add(userAccount)
        prefs.selectedAccount = userAccount.id
        prefs.accounts = newAccounts
    }


    private fun getQueryParameterFromUri(url: String, queryName: String): String {
        val uri = URI(url)
        val query = uri.query
        val parameters = query.split("&")

        var code = ""
        for (parameter in parameters) {
            if (parameter.startsWith(queryName)) {
                code = parameter.substring(queryName.length + 1)
                break
            }
        }
        return code
    }

    private fun switchAccount(newAccount: UserAccount?) {
        Toothpick.closeScope(DI.SERVER_SCOPE)
        Toothpick
            .openScopes(DI.APP_SCOPE, DI.SERVER_SCOPE)
            .installModules(ServerModule(newAccount))
    }

    companion object {
        private const val PARAMETER_CODE = "code"
    }
}
