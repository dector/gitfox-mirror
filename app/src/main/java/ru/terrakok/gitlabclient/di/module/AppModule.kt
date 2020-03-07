package ru.terrakok.gitlabclient.di.module

import android.content.Context
import android.content.res.AssetManager
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.di.AppDevelopersPath
import ru.terrakok.gitlabclient.di.CacheLifetime
import ru.terrakok.gitlabclient.di.DefaultPageSize
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.app.develop.AppInfo
import ru.terrakok.gitlabclient.entity.app.session.OAuthParams
import ru.terrakok.gitlabclient.model.data.server.client.OkHttpClientFactory
import ru.terrakok.gitlabclient.model.system.AppSchedulers
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.model.system.message.SystemMessageNotifier
import ru.terrakok.gitlabclient.util.Base64Tools
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.06.17.
 */
class AppModule(context: Context) : Module() {
    init {
        // Global
        bind(Context::class.java).toInstance(context)
        bind(String::class.java).withName(AppDevelopersPath::class.java).toInstance(BuildConfig.APP_DEVELOPERS_PATH)
        bind(PrimitiveWrapper::class.java).withName(DefaultPageSize::class.java).toInstance(PrimitiveWrapper(20))
        bind(PrimitiveWrapper::class.java).withName(CacheLifetime::class.java).toInstance(PrimitiveWrapper(300_000L))
        bind(SchedulersProvider::class.java).toInstance(AppSchedulers())
        bind(ResourceManager::class.java).singleton()
        bind(Base64Tools::class.java).toInstance(Base64Tools())
        bind(AssetManager::class.java).toInstance(context.assets)
        bind(SystemMessageNotifier::class.java).toInstance(SystemMessageNotifier())
        bind(Json::class.java).toInstance(Json(JsonConfiguration.Stable.copy(
            ignoreUnknownKeys = true,
            isLenient = true,
            encodeDefaults = false
        )))
        bind(OkHttpClientFactory::class.java).singleton()

        // Navigation
        val cicerone = Cicerone.create()
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)

        // AppInfo
        bind(AppInfo::class.java).toInstance(
            AppInfo(
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE,
                BuildConfig.APP_DESCRIPTION,
                BuildConfig.VERSION_UID.take(8),
                BuildConfig.APP_HOME_PAGE,
                BuildConfig.FEEDBACK_URL
            )
        )

        // Auth
        bind(OAuthParams::class.java).toInstance(
            OAuthParams(
                BuildConfig.ORIGIN_GITLAB_ENDPOINT,
                BuildConfig.OAUTH_APP_ID,
                BuildConfig.OAUTH_SECRET,
                BuildConfig.OAUTH_CALLBACK
            )
        )
    }
}
