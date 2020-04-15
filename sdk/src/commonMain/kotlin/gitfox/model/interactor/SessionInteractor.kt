package gitfox.model.interactor

import com.benasher44.uuid.uuid4
import gitfox.entity.app.session.OAuthParams
import gitfox.entity.app.session.UserAccount
import gitfox.model.data.server.UserAccountApi
import gitfox.model.data.state.SessionSwitcher
import gitfox.model.data.storage.Prefs
import io.ktor.http.Url

class SessionInteractor internal constructor(
    private val prefs: Prefs,
    private val oauthParams: OAuthParams,
    private val userAccountApi: UserAccountApi,
    private val sessionSwitcher: SessionSwitcher
) {
    private val hash = uuid4().toString()

    val oauthUrl = "${oauthParams.endpoint}oauth/authorize?client_id=${oauthParams.appId}" +
        "&redirect_uri=${oauthParams.redirectUrl}&response_type=code&state=$hash"

    fun checkOAuthRedirect(url: String) = url.indexOf(oauthParams.redirectUrl) == 0

    fun getCurrentUserAccount(): UserAccount? = prefs.getCurrentUserAccount()

    fun setCurrentUserAccount(accountId: String): UserAccount? {
        val account = prefs.accounts.find { it.id == accountId }
        prefs.selectedAccount = account?.id
        sessionSwitcher.initSession(account)
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
        val newAccount = logout(accountId)
        sessionSwitcher.initSession(newAccount)
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

    private fun openNewAccount(userAccount: UserAccount) {
        val newAccounts = prefs.accounts.toMutableList()
        newAccounts.removeAll { it.id == userAccount.id }
        newAccounts.add(userAccount)
        prefs.selectedAccount = userAccount.id
        prefs.accounts = newAccounts
        sessionSwitcher.initSession(userAccount)
    }

    private fun getQueryParameterFromUri(url: String, queryName: String) =
        Url(url).parameters[queryName] ?: ""

    companion object {
        private const val PARAMETER_CODE = "code"
    }
}
