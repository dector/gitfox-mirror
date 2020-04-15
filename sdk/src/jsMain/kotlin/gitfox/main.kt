package gitfox

import com.github.aakira.napier.Napier
import gitfox.entity.app.session.OAuthParams
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() {
    val sdk = SDK.create(
        oAuthParams = OAuthParams("https://gitlab.com/", "", "", ""),
        isDebug = true
    )

    GlobalScope.launch {
        sdk.getSessionInteractor().loginOnCustomServer(
            "https://gitlab.com/",
            "" //todo put real private token!
        )
        Napier.v("Hello, " + sdk.getAccountInteractor().getMyProfile().name + "!")
        Napier.v("It's your todos: " + sdk.getAccountInteractor().getMyTodos(false, 0))
    }
}