package ru.terrakok.gitlabclient.toothpick.module

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.ServerConfig
import ru.terrakok.gitlabclient.model.data.storage.Prefs
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.model.interactor.event.EventInteractor
import ru.terrakok.gitlabclient.model.interactor.issue.IssuesInteractor
import ru.terrakok.gitlabclient.model.interactor.mergerequest.MergeRequestListInteractor
import ru.terrakok.gitlabclient.model.interactor.profile.MyProfileInteractor
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.model.interactor.projects.MainProjectsListInteractor
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository
import ru.terrakok.gitlabclient.model.repository.event.EventRepository
import ru.terrakok.gitlabclient.model.repository.issue.IssueRepository
import ru.terrakok.gitlabclient.model.repository.merge_request.MergeRequestRepository
import ru.terrakok.gitlabclient.model.repository.profile.ProfileRepository
import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository
import ru.terrakok.gitlabclient.model.system.AppSchedulers
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.provider.ApiProvider
import ru.terrakok.gitlabclient.toothpick.provider.OkHttpClientProvider
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultPageSize
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.06.17.
 */
class AppModule(context: Context) : Module() {
    init {
        //Global
        bind(Context::class.java).toInstance(context)
        bind(PrimitiveWrapper::class.java).withName(DefaultPageSize::class.java).toInstance(PrimitiveWrapper(20))
        bind(SchedulersProvider::class.java).toInstance(AppSchedulers())
        bind(ResourceManager::class.java).singletonInScope()

        //Network
        bind(Gson::class.java).toInstance(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create())
        bind(OkHttpClient::class.java).toProvider(OkHttpClientProvider::class.java).singletonInScope()
        bind(ServerConfig::class.java).toInstance(ServerConfig())
        bind(GitlabApi::class.java).toProvider(ApiProvider::class.java).singletonInScope()

        //Auth
        bind(AuthHolder::class.java).to(Prefs::class.java).singletonInScope()
        bind(AuthRepository::class.java).singletonInScope()
        bind(AuthInteractor::class.java).singletonInScope()

        //Navigation
        val cicerone = Cicerone.create()
        bind(Router::class.java).toInstance(cicerone.router)
        bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)

        //Profile
        bind(ProfileRepository::class.java)
        bind(MyProfileInteractor::class.java)

        //Project
        bind(ProjectRepository::class.java)
        bind(MainProjectsListInteractor::class.java)
        bind(ProjectInteractor::class.java)

        //Issue
        bind(IssueRepository::class.java)
        bind(IssuesInteractor::class.java)

        //Event
        bind(EventRepository::class.java)
        bind(EventInteractor::class.java)

        //Merge request
        bind(MergeRequestRepository::class.java)
        bind(MergeRequestListInteractor::class.java)
    }
}