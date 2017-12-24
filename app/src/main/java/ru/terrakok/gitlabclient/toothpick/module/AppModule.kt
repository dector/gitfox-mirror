package ru.terrakok.gitlabclient.toothpick.module

import android.content.Context
import android.content.res.AssetManager
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.entity.app.develop.AppInfo
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.model.data.storage.Prefs
import ru.terrakok.gitlabclient.model.data.storage.RawAppData
import ru.terrakok.gitlabclient.model.interactor.app.AppInfoInteractor
import ru.terrakok.gitlabclient.model.interactor.project.Base64Tools
import ru.terrakok.gitlabclient.model.interactor.project.MarkDownConverter
import ru.terrakok.gitlabclient.model.repository.app.AppInfoRepository
import ru.terrakok.gitlabclient.model.system.AppSchedulers
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultPageSize
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultServerPath
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.06.17.
 */
class AppModule(context: Context) : Module() {
    init {
        //Global
        bind(Context::class.java).toInstance(context)
        bind(String::class.java).withName(DefaultServerPath::class.java).toInstance(BuildConfig.ORIGIN_GITLAB_ENDPOINT)
        bind(PrimitiveWrapper::class.java).withName(DefaultPageSize::class.java).toInstance(PrimitiveWrapper(20))
        bind(SchedulersProvider::class.java).toInstance(AppSchedulers())
        bind(ResourceManager::class.java).singletonInScope()
        bind(MarkDownConverter::class.java).toInstance(MarkDownConverter())
        bind(Base64Tools::class.java).toInstance(Base64Tools())
        bind(AssetManager::class.java).toInstance(context.assets)
        bind(RawAppData::class.java)

        //Navigation
        val cicerone = Cicerone.create()
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)

        //Auth
        bind(AuthHolder::class.java).to(Prefs::class.java).singletonInScope()

        //AppInfo
        bind(AppInfo::class.java).toInstance(AppInfo(
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE,
                BuildConfig.APP_DESCRIPTION,
                BuildConfig.VERSION_UID.take(8),
                BuildConfig.APP_HOME_PAGE,
                BuildConfig.FEEDBACK_URL
        ))
        bind(AppInfoRepository::class.java)
        bind(AppInfoInteractor::class.java)
    }
}