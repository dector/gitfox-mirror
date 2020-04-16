package gitfox

import com.github.aakira.napier.Napier
import com.russhwolf.settings.JsSettings
import gitfox.client.HttpClientFactory
import gitfox.entity.app.session.OAuthParams
import gitfox.util.JsAntilog
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//this is simple sample for real javascript project you need to use JsSDK
fun main() {
    Napier.base(JsAntilog())
    val sdk = SDK(
        SDK.defaultPageSize,
        SDK.cacheLifetime,
        OAuthParams("https://gitlab.com/", "", "", ""),
        true,
        HttpClientFactory(),
        JsSettings()
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