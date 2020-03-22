package ru.terrakok.gitlabclient.di.module

import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.di.ServerPath
import ru.terrakok.gitlabclient.di.provider.*
import ru.terrakok.gitlabclient.entity.app.session.AuthHolder
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.model.data.cache.ProjectCache
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
import ru.terrakok.gitlabclient.model.interactor.*
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
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
            // For authorization screen
            bind(AuthHolder::class.java).toInstance(AuthHolder(null, true))
            bind(String::class.java).withName(ServerPath::class.java).toInstance(BuildConfig.ORIGIN_GITLAB_ENDPOINT)
        }

        // Network
        bind(ProjectCache::class.java).toInstance(ProjectCache(300_000L))
        bind(ServerChanges::class.java).toInstance(ServerChanges())
        bind(GitlabApi::class.java).toProvider(ApiProvider::class.java).providesSingleton()
        bind(MarkDownConverter::class.java).toProvider(MarkDownConverterProvider::class.java)
            .providesSingleton()

        // Error handler with logout logic
        bind(ErrorHandler::class.java).singleton()

        //Interactors
        bind(AccountInteractor::class.java).toProvider(AccountInteractorProvider::class.java)
        bind(AppInfoInteractor::class.java).toProvider(AppInfoInteractorProvider::class.java)
        bind(CommitInteractor::class.java).toProvider(CommitInteractorProvider::class.java)
        bind(EventInteractor::class.java).toProvider(EventInteractorProvider::class.java)
        bind(IssueInteractor::class.java).toProvider(IssueInteractorProvider::class.java)
        bind(LabelInteractor::class.java).toProvider(LabelInteractorProvider::class.java)
        bind(MembersInteractor::class.java).toProvider(MembersInteractorProvider::class.java)
        bind(MergeRequestInteractor::class.java).toProvider(MergeRequestInteractorProvider::class.java)
        bind(MilestoneInteractor::class.java).toProvider(MilestoneInteractorProvider::class.java)
        bind(ProjectInteractor::class.java).toProvider(ProjectInteractorProvider::class.java)
        bind(SessionInteractor::class.java).toProvider(SessionInteractorProvider::class.java)
        bind(TodoInteractor::class.java).toProvider(TodoInteractorProvider::class.java)
        bind(UserInteractor::class.java).toProvider(UserInteractorProvider::class.java)
    }
}
