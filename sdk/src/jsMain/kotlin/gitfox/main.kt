package gitfox

import gitfox.entity.app.develop.AppInfo
import gitfox.entity.app.session.OAuthParams
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

fun main() {
    println("Hello terrakok!")
    val sdk = SDK(
        "https://gitlab.com/",
        oAuthParams = OAuthParams("", "", "", ""),
        appInfo = AppInfo("jsVersion", 42, "js test", "local", "", ""),
        getLibraries = { emptyList() },
        isDebug = true
    )
    println(sdk.getAppInfoInteractor().getAppInfo().versionName)

    GlobalScope.launch {
        sdk.getSessionInteractor().loginOnCustomServer(
            "https://gitlab.com/",
            "test-token" //todo add real token
        )
        val events = sdk.getEventInteractor().getEvents(page = 0)
        println("!!!!!!!!!!!!!EVENTS!!!!!!!!!!!!!")
        println(events)
    }
}