package gitfox

import android.content.Context
import com.russhwolf.settings.AndroidSettings
import gitfox.client.HttpClientFactory
import gitfox.entity.app.session.OAuthParams

fun SDK.Companion.create(
    context: Context,
    oAuthParams: OAuthParams,
    isDebug: Boolean
) = SDK(
    defaultPageSize,
    cacheLifetime,
    oAuthParams,
    isDebug,
    HttpClientFactory(context),
    AndroidSettings(context.getSharedPreferences("gf_prefs", Context.MODE_PRIVATE))
)