package ru.terrakok.gitlabclient.di.module

import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.di.ServerPath
import ru.terrakok.gitlabclient.di.provider.ApiProvider
import ru.terrakok.gitlabclient.di.provider.MarkDownConverterProvider
import ru.terrakok.gitlabclient.entity.app.session.AuthHolder
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.model.data.cache.ProjectCache
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
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
        bind(ProjectCache::class.java).singleton()
        bind(ServerChanges::class.java).singleton()
        bind(GitlabApi::class.java).toProvider(ApiProvider::class.java).providesSingleton()
        bind(MarkDownConverter::class.java).toProvider(MarkDownConverterProvider::class.java)
            .providesSingleton()

        // Error handler with logout logic
        bind(ErrorHandler::class.java).singleton()
    }
}
