package gitfox

import gitfox.entity.app.develop.AppInfo
import gitfox.entity.app.session.OAuthParams
import gitfox.entity.app.session.UserAccount
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

fun main() {
    val sdk = SDK(
        "https://gitlab.com/",
        oAuthParams = OAuthParams("", "", "", ""),
        appInfo = AppInfo("", 42, "", "", "", ""),
        getLibraries = { emptyList() },
        isDebug = true
    )

    GlobalScope.launch {
        sdk.getSessionInteractor().loginOnCustomServer(
            "https://gitlab.com/",
            ""
        )
        println("Hello, " + sdk.getAccountInteractor().getMyProfile().name + "!")
        println("It's your todos: " + sdk.getAccountInteractor().getMyTodos(false, 0))
    }
}