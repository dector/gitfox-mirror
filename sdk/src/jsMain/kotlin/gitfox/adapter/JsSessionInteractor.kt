package gitfox.adapter

import gitfox.model.interactor.SessionInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

class JsSessionInteractor internal constructor(
    private val interactor: SessionInteractor
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    @JsName("oauthUrl")
    val oauthUrl: String
        get() = interactor.oauthUrl

    @JsName("checkOAuthRedirect")
    fun checkOAuthRedirect(url: String) = interactor.checkOAuthRedirect(url)

    @JsName("getCurrentUserAccount")
    fun getCurrentUserAccount() = interactor.getCurrentUserAccount()

    @JsName("setCurrentUserAccount")
    fun setCurrentUserAccount(accountId: String) = interactor.setCurrentUserAccount(accountId)

    @JsName("getUserAccounts")
    fun getUserAccounts() = interactor.getUserAccounts()

    @JsName("logout")
    fun logout() = interactor.logout()

    @JsName("logoutFromAccount")
    fun logoutFromAccount(accountId: String) = interactor.logoutFromAccount(accountId)

    @JsName("login")
    fun login(oauthRedirect: String) = promise {
        interactor.login(oauthRedirect)
    }

    @JsName("loginOnCustomServer")
    fun loginOnCustomServer(
        serverPath: String,
        token: String
    ) = promise {
        interactor.loginOnCustomServer(serverPath, token)
    }
}