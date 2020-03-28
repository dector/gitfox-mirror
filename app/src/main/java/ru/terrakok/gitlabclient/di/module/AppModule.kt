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
import ru.terrakok.gitlabclient.di.DefaultPageSize
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.provider.AppInfoInteractorProvider
import ru.terrakok.gitlabclient.di.provider.LaunchInteractorProvider
import ru.terrakok.gitlabclient.di.provider.PrefsProvider
import ru.terrakok.gitlabclient.di.provider.UserAccountApiProvider
import ru.terrakok.gitlabclient.entity.app.develop.AppInfo
import ru.terrakok.gitlabclient.entity.app.session.OAuthParams
import ru.terrakok.gitlabclient.model.data.server.UserAccountApi
import ru.terrakok.gitlabclient.model.data.server.client.HttpClientFactory
import ru.terrakok.gitlabclient.model.data.state.SessionSwitcher
import ru.terrakok.gitlabclient.model.data.storage.Prefs
import ru.terrakok.gitlabclient.model.interactor.AppInfoInteractor
import ru.terrakok.gitlabclient.model.interactor.LaunchInteractor
import ru.terrakok.gitlabclient.model.system.ResourceManager
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
        bind(ResourceManager::class.java).singleton()
        bind(Base64Tools::class.java).toInstance(Base64Tools())
        bind(AssetManager::class.java).toInstance(context.assets)
        bind(SystemMessageNotifier::class.java).toInstance(SystemMessageNotifier())
        val json = Json(JsonConfiguration.Stable.copy(
            ignoreUnknownKeys = true,
            isLenient = true,
            encodeDefaults = false
        ))
        bind(Json::class.java).toInstance(json)
        bind(HttpClientFactory::class.java).toInstance(HttpClientFactory(context, json))
        bind(Prefs::class.java).toProvider(PrefsProvider::class.java)
        bind(UserAccountApi::class.java).toProvider(UserAccountApiProvider::class.java)
        bind(SessionSwitcher::class.java).toInstance(SessionSwitcher())

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

        bind(LaunchInteractor::class.java).toProvider(LaunchInteractorProvider::class.java)
        bind(AppInfoInteractor::class.java).toProvider(AppInfoInteractorProvider::class.java)
    }
}
