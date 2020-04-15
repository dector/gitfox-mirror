package gitfox

import com.github.aakira.napier.Antilog
import com.github.aakira.napier.Napier
import com.russhwolf.settings.JsSettings
import gitfox.client.HttpClientFactory
import gitfox.entity.app.session.OAuthParams

fun SDK.Companion.create(
    oAuthParams: OAuthParams,
    isDebug: Boolean
): SDK = JsSDK(oAuthParams, isDebug)

private class JsSDK(
    oAuthParams: OAuthParams,
    isDebug: Boolean
) : SDK(
    defaultPageSize,
    cacheLifetime,
    oAuthParams,
    isDebug,
    HttpClientFactory(),
    JsSettings()
) {

    init {
        if (isDebug) Napier.base(
            object : Antilog() {
                override fun performLog(priority: Napier.Level, tag: String?, throwable: Throwable?, message: String?) {
                    val logTag = "GitFox"

                    val fullMessage = if (message != null) {
                        if (throwable != null) "$message\n${throwable.message}"
                        else message
                    } else throwable?.message ?: return

                    when (priority) {
                        Napier.Level.VERBOSE -> console.log("VERBOSE $logTag : $fullMessage")
                        Napier.Level.DEBUG -> console.log("DEBUG $logTag : $fullMessage")
                        Napier.Level.INFO -> console.info("INFO $logTag : $fullMessage")
                        Napier.Level.WARNING -> console.warn("WARNING $logTag : $fullMessage")
                        Napier.Level.ERROR -> console.error("ERROR $logTag : $fullMessage")
                        Napier.Level.ASSERT -> console.error("ASSERT $logTag : $fullMessage")
                    }
                }
            }
        )
    }
}