package ru.terrakok.gitlabclient.toothpick.module

import com.google.gson.Gson
import okhttp3.OkHttpClient
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.MarkDownUrlResolver
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.model.interactor.auth.OAuthParams
import ru.terrakok.gitlabclient.model.interactor.event.EventInteractor
import ru.terrakok.gitlabclient.model.interactor.issue.IssueInteractor
import ru.terrakok.gitlabclient.model.interactor.mergerequest.MergeRequestInteractor
import ru.terrakok.gitlabclient.model.interactor.profile.MyProfileInteractor
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.model.interactor.todo.TodoListInteractor
import ru.terrakok.gitlabclient.model.interactor.user.UserInteractor
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository
import ru.terrakok.gitlabclient.model.repository.event.EventRepository
import ru.terrakok.gitlabclient.model.repository.issue.IssueRepository
import ru.terrakok.gitlabclient.model.repository.mergerequest.MergeRequestRepository
import ru.terrakok.gitlabclient.model.repository.profile.ProfileRepository
import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository
import ru.terrakok.gitlabclient.model.repository.todo.TodoRepository
import ru.terrakok.gitlabclient.model.repository.user.UserRepository
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.toothpick.provider.ApiProvider
import ru.terrakok.gitlabclient.toothpick.provider.GsonProvider
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
        bind(Gson::class.java).toProvider(GsonProvider::class.java).providesSingletonInScope()
        bind(OkHttpClient::class.java).toProvider(OkHttpClientProvider::class.java).providesSingletonInScope()
        bind(GitlabApi::class.java).toProvider(ApiProvider::class.java).providesSingletonInScope()
        bind(MarkDownUrlResolver::class.java)

        //Auth
        bind(OAuthParams::class.java).toInstance(
            OAuthParams(BuildConfig.OAUTH_APP_ID, BuildConfig.OAUTH_SECRET, BuildConfig.OAUTH_CALLBACK)
        )
        bind(AuthRepository::class.java).singletonInScope()
        bind(AuthInteractor::class.java).singletonInScope()

        //Error handler with logout logic
        bind(ErrorHandler::class.java).singletonInScope()

        //Profile
        bind(ProfileRepository::class.java)
        bind(MyProfileInteractor::class.java)

        //Project
        bind(ProjectRepository::class.java)
        bind(ProjectInteractor::class.java)

        //Issue
        bind(IssueRepository::class.java)
        bind(IssueInteractor::class.java)

        //Event
        bind(EventRepository::class.java)
        bind(EventInteractor::class.java)

        //Merge request
        bind(MergeRequestRepository::class.java)
        bind(MergeRequestInteractor::class.java)

        //User info
        bind(UserRepository::class.java)
        bind(UserInteractor::class.java)

        //Todos
        bind(TodoRepository::class.java)
        bind(TodoListInteractor::class.java)
    }
}