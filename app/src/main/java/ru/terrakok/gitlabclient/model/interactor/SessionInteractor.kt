package ru.terrakok.gitlabclient.model.interactor

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.terrakok.gitlabclient.di.DI
import ru.terrakok.gitlabclient.di.module.ServerModule
import ru.terrakok.gitlabclient.entity.app.session.OAuthParams
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.model.data.cache.ProjectCache
import ru.terrakok.gitlabclient.model.data.server.UserAccountApi
import ru.terrakok.gitlabclient.model.data.storage.Prefs
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import toothpick.Toothpick
import java.net.URI
import java.util.*
import javax.inject.Inject

class SessionInteractor @Inject constructor(
    private val prefs: Prefs,
    private val oauthParams: OAuthParams,
    private val userAccountApi: UserAccountApi,
    private val projectCache: ProjectCache,
    private val schedulers: SchedulersProvider
) {
    private val hash = UUID.randomUUID().toString()

    val oauthUrl = "${oauthParams.endpoint}oauth/authorize?client_id=${oauthParams.appId}" +
            "&redirect_uri=${oauthParams.redirectUrl}&response_type=code&state=$hash"

    fun checkOAuthRedirect(url: String) = url.indexOf(oauthParams.redirectUrl) == 0

    fun getCurrentUserAccount(): UserAccount? = prefs.getCurrentUserAccount()

    fun setCurrentUserAccount(accountId: String): UserAccount? {
        val account = prefs.accounts.find { it.id == accountId }
        prefs.selectedAccount = account?.id
        projectCache.clear()
        initNewSession(account)
        return account
    }

    fun getUserAccounts(): List<UserAccount> = prefs.accounts

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
        initNewSession(newAccount)
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

    suspend fun login(oauthRedirect: String) {
        if (oauthRedirect.contains(hash)) {
            val account = userAccountApi.requestUserAccount(
                oauthParams.endpoint,
                oauthParams.appId,
                oauthParams.appKey,
                getQueryParameterFromUri(oauthRedirect, PARAMETER_CODE),
                oauthParams.redirectUrl
            )
            openNewAccount(account)
        } else {
            throw  RuntimeException("Not valid oauth hash!")
        }
    }

    suspend fun loginOnCustomServer(serverPath: String, token: String) {
        val account = userAccountApi.requestUserAccount(serverPath, token)
        openNewAccount(account)
    }

    private suspend fun openNewAccount(userAccount: UserAccount) {
        withContext(Dispatchers.Main) {
            val newAccounts = prefs.accounts.toMutableList()
            newAccounts.removeAll { it.id == userAccount.id }
            newAccounts.add(userAccount)
            prefs.selectedAccount = userAccount.id
            prefs.accounts = newAccounts
            initNewSession(userAccount)
        }
    }

    private fun initNewSession(newAccount: UserAccount?) {
        Toothpick.closeScope(DI.SERVER_SCOPE)
        Toothpick
            .openScopes(DI.APP_SCOPE, DI.SERVER_SCOPE)
            .installModules(ServerModule(newAccount))
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

    companion object {
        private const val PARAMETER_CODE = "code"
    }
}
