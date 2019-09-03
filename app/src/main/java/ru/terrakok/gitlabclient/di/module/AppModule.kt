package ru.terrakok.gitlabclient.di.module

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.di.*
import ru.terrakok.gitlabclient.di.provider.GsonProvider
import ru.terrakok.gitlabclient.entity.app.develop.AppInfo
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
        bind(String::class.java).withName(DefaultServerPath::class.java).toInstance(BuildConfig.ORIGIN_GITLAB_ENDPOINT)
        bind(String::class.java).withName(AppDevelopersPath::class.java).toInstance(BuildConfig.APP_DEVELOPERS_PATH)
        bind(PrimitiveWrapper::class.java).withName(DefaultPageSize::class.java).toInstance(PrimitiveWrapper(20))
        bind(PrimitiveWrapper::class.java).withName(CacheLifetime::class.java).toInstance(PrimitiveWrapper(300_000L))
        bind(SchedulersProvider::class.java).toInstance(AppSchedulers())
        bind(ResourceManager::class.java).singletonInScope()
        bind(Base64Tools::class.java).toInstance(Base64Tools())
        bind(AssetManager::class.java).toInstance(context.assets)
        bind(SystemMessageNotifier::class.java).toInstance(SystemMessageNotifier())
        bind(Gson::class.java).toProvider(GsonProvider::class.java).providesSingletonInScope()

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
    }
}