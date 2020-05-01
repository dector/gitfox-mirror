package gitfox

import android.content.Context
import com.russhwolf.settings.AndroidSettings
import gitfox.entity.app.session.OAuthParams
import gitfox.util.HttpClientFactory
import gitfox.util.createHttpEngine

fun SDK.Companion.create(
    context: Context,
    oAuthParams: OAuthParams,
    isDebug: Boolean
) = SDK(
    defaultPageSize,
    cacheLifetime,
    oAuthParams,
    isDebug,
    HttpClientFactory { cacheSize, timeout -> createHttpEngine(context, cacheSize, timeout) },
    AndroidSettings(context.getSharedPreferences("gf_prefs", Context.MODE_PRIVATE))
)