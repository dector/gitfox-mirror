package ru.terrakok.gitlabclient.toothpick.module

import okhttp3.OkHttpClient
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.entity.app.session.AuthHolder
import ru.terrakok.gitlabclient.entity.app.session.OAuthParams
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.model.data.cache.ProjectCache
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.MarkDownUrlResolver
import ru.terrakok.gitlabclient.model.interactor.event.EventInteractor
import ru.terrakok.gitlabclient.model.interactor.issue.IssueInteractor
import ru.terrakok.gitlabclient.model.interactor.mergerequest.MergeRequestInteractor
import ru.terrakok.gitlabclient.model.interactor.milestone.MilestoneInteractor
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.model.interactor.todo.TodoListInteractor
import ru.terrakok.gitlabclient.model.interactor.user.UserInteractor
import ru.terrakok.gitlabclient.model.repository.event.EventRepository
import ru.terrakok.gitlabclient.model.repository.issue.IssueRepository
import ru.terrakok.gitlabclient.model.repository.mergerequest.MergeRequestRepository
import ru.terrakok.gitlabclient.model.repository.milestone.MilestoneRepository
import ru.terrakok.gitlabclient.model.repository.profile.ProfileRepository
import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository
import ru.terrakok.gitlabclient.model.repository.todo.TodoRepository
import ru.terrakok.gitlabclient.model.repository.user.UserRepository
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.toothpick.provider.ApiProvider
import ru.terrakok.gitlabclient.toothpick.provider.MarkDownConverterProvider
import ru.terrakok.gitlabclient.toothpick.provider.OkHttpClientProvider
import ru.terrakok.gitlabclient.toothpick.provider.OkHttpClientWithErrorHandlerProvider
import ru.terrakok.gitlabclient.toothpick.qualifier.ServerPath
import ru.terrakok.gitlabclient.toothpick.qualifier.WithErrorHandler
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.06.17.
 */
class ServerModule(userAccount: UserAccount?) : Module() {
    init {

        if (userAccount != null) {
            bind(AuthHolder::class.java).toInstance(AuthHolder(userAccount.token, userAccount.isOAuth))
            bind(String::class.java).withName(ServerPath::class.java).toInstance(userAccount.serverPath)
        } else {
            //for authorization screen
            bind(AuthHolder::class.java).toInstance(AuthHolder(null, true))
            bind(String::class.java).withName(ServerPath::class.java).toInstance(BuildConfig.ORIGIN_GITLAB_ENDPOINT)
        }

        //Network
        bind(OkHttpClient::class.java).toProvider(OkHttpClientProvider::class.java).providesSingletonInScope()
        bind(OkHttpClient::class.java).withName(WithErrorHandler::class.java)
            .toProvider(OkHttpClientWithErrorHandlerProvider::class.java)
            .providesSingletonInScope()
        bind(ProjectCache::class.java).singletonInScope()
        bind(GitlabApi::class.java).toProvider(ApiProvider::class.java).providesSingletonInScope()
        bind(MarkDownConverter::class.java).toProvider(MarkDownConverterProvider::class.java).providesSingletonInScope()
        bind(MarkDownUrlResolver::class.java)

        //Auth
        bind(OAuthParams::class.java).toInstance(
            OAuthParams(
                BuildConfig.OAUTH_APP_ID,
                BuildConfig.OAUTH_SECRET,
                BuildConfig.OAUTH_CALLBACK
            )
        )

        //Error handler with logout logic
        bind(ErrorHandler::class.java).singletonInScope()

        //Profile
        bind(ProfileRepository::class.java)

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

        //Milestone
        bind(MilestoneRepository::class.java)
        bind(MilestoneInteractor::class.java)

        //User info
        bind(UserRepository::class.java)
        bind(UserInteractor::class.java)

        //Todos
        bind(TodoRepository::class.java)
        bind(TodoListInteractor::class.java)
    }
}