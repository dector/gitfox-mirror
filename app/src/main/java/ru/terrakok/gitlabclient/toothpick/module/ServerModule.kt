package ru.terrakok.gitlabclient.toothpick.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.model.interactor.auth.OAuthParams
import ru.terrakok.gitlabclient.model.interactor.event.EventInteractor
import ru.terrakok.gitlabclient.model.interactor.issue.IssuesInteractor
import ru.terrakok.gitlabclient.model.interactor.profile.MyProfileInteractor
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.model.interactor.projects.MainProjectsListInteractor
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository
import ru.terrakok.gitlabclient.model.repository.event.EventRepository
import ru.terrakok.gitlabclient.model.repository.issue.IssueRepository
import ru.terrakok.gitlabclient.model.repository.profile.ProfileRepository
import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository
import ru.terrakok.gitlabclient.model.system.ServerSwitcher
import ru.terrakok.gitlabclient.toothpick.provider.ApiProvider
import ru.terrakok.gitlabclient.toothpick.provider.OkHttpClientProvider
import ru.terrakok.gitlabclient.toothpick.qualifier.ServerPath
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.06.17.
 */
class ServerModule(serverUrl: String) : Module() {
    init {
        //Network
        bind(String::class.java).withName(ServerPath::class.java).toInstance(serverUrl)
        bind(Gson::class.java).toInstance(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create())
        bind(OkHttpClient::class.java).toProvider(OkHttpClientProvider::class.java).singletonInScope()
        bind(GitlabApi::class.java).toProvider(ApiProvider::class.java).singletonInScope()
        bind(ServerSwitcher::class.java).singletonInScope()

        //Auth
        //todo: before release change and move to private config
        bind(OAuthParams::class.java).toInstance(OAuthParams(
                "808b7f51c6634294afd879edd75d5eaf55f1a75e7fe5bd91ca8b7140a5af639d",
                "a9dd39c8d2e781b65814007ca0f8b555d34f79b4d30c9356c38bb7ad9909c6f3",
                "app://gitlab.client/"
        ))
        bind(AuthRepository::class.java).singletonInScope()
        bind(AuthInteractor::class.java).singletonInScope()

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
    }
}