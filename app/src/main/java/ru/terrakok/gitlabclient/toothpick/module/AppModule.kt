package ru.terrakok.gitlabclient.toothpick.module

import android.content.Context
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.model.data.storage.Prefs
import ru.terrakok.gitlabclient.model.system.AppSchedulers
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.model.repository.mergerequest.MergeRequestRepository
import ru.terrakok.gitlabclient.model.interactor.mergerequest.MergeRequestListInteractor
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

        //Navigation
        val cicerone = Cicerone.create()
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)

        //Auth
        bind(AuthHolder::class.java).to(Prefs::class.java).singletonInScope()

        //Merge request
        bind(MergeRequestRepository::class.java)
        bind(MergeRequestListInteractor::class.java)
    }
}