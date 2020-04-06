package gitfox

import com.github.aakira.napier.DebugAntilog
import com.github.aakira.napier.Napier
import gitfox.entity.app.session.OAuthParams
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() {
    Napier.base(DebugAntilog("GitFox"))
    val sdk = SDK(
        "https://gitlab.com/",
        oAuthParams = OAuthParams("", "", "", ""),
        isDebug = true
    )

    GlobalScope.launch {
        sdk.getSessionInteractor().loginOnCustomServer(
            "https://gitlab.com/",
            "" //todo put real private token!
        )
        println("Hello, " + sdk.getAccountInteractor().getMyProfile().name + "!")
        println("It's your todos: " + sdk.getAccountInteractor().getMyTodos(false, 0))
    }
}