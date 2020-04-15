package gitfox.adapter

import gitfox.model.interactor.SessionInteractor
import kotlinx.coroutines.CoroutineScope

class IosSessionInteractor internal constructor(
    private val interactor: SessionInteractor
) : CoroutineScope by CoroutineScope(MainLoopDispatcher) {

    val oauthUrl: String get() = interactor.oauthUrl
    fun checkOAuthRedirect(url: String) = interactor.checkOAuthRedirect(url)
    fun getCurrentUserAccount() = interactor.getCurrentUserAccount()
    fun setCurrentUserAccount(accountId: String) = interactor.setCurrentUserAccount(accountId)
    fun getUserAccounts() = interactor.getUserAccounts()
    fun logout() = interactor.logout()
    fun logoutFromAccount(accountId: String) = interactor.logoutFromAccount(accountId)

    fun login(oauthRedirect: String, callback: (result: Unit?, error: Exception?) -> Unit) {
        fire(callback) { interactor.login(oauthRedirect) }
    }

    fun loginOnCustomServer(
        serverPath: String,
        token: String,
        callback: (result: Unit?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.loginOnCustomServer(serverPath, token) }
    }
}