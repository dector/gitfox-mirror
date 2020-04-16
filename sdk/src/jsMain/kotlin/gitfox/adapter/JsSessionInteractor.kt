package gitfox.adapter

import gitfox.model.interactor.SessionInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

class JsSessionInteractor internal constructor(
    private val interactor: SessionInteractor
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    val oauthUrl: String get() = interactor.oauthUrl
    fun checkOAuthRedirect(url: String) = interactor.checkOAuthRedirect(url)
    fun getCurrentUserAccount() = interactor.getCurrentUserAccount()
    fun setCurrentUserAccount(accountId: String) = interactor.setCurrentUserAccount(accountId)
    fun getUserAccounts() = interactor.getUserAccounts()
    fun logout() = interactor.logout()
    fun logoutFromAccount(accountId: String) = interactor.logoutFromAccount(accountId)

    fun login(oauthRedirect: String) = promise {
        interactor.login(oauthRedirect)
    }

    fun loginOnCustomServer(
        serverPath: String,
        token: String
    ) = promise {
        interactor.loginOnCustomServer(serverPath, token)
    }
}