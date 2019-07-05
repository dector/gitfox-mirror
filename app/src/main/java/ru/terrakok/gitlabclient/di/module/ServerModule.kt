package ru.terrakok.gitlabclient.di.module

import okhttp3.OkHttpClient
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.di.ServerPath
import ru.terrakok.gitlabclient.di.WithErrorHandler
import ru.terrakok.gitlabclient.di.provider.ApiProvider
import ru.terrakok.gitlabclient.di.provider.MarkDownConverterProvider
import ru.terrakok.gitlabclient.di.provider.OkHttpClientProvider
import ru.terrakok.gitlabclient.di.provider.OkHttpClientWithErrorHandlerProvider
import ru.terrakok.gitlabclient.di.provider.LabelSpanConfigProvider
import ru.terrakok.gitlabclient.entity.app.session.AuthHolder
import ru.terrakok.gitlabclient.entity.app.session.OAuthParams
import ru.terrakok.gitlabclient.entity.app.session.UserAccount
import ru.terrakok.gitlabclient.model.data.cache.ProjectCache
import ru.terrakok.gitlabclient.model.data.cache.ProjectLabelCache
import ru.terrakok.gitlabclient.markwonx.label.LabelSpanConfig
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
        bind(OkHttpClient::class.java).toProvider(OkHttpClientProvider::class.java).providesSingletonInScope()
        bind(OkHttpClient::class.java).withName(WithErrorHandler::class.java)
            .toProvider(OkHttpClientWithErrorHandlerProvider::class.java)
            .providesSingletonInScope()
        bind(ProjectCache::class.java).singletonInScope()
        bind(ServerChanges::class.java).singletonInScope()
        bind(ProjectLabelCache::class.java).singletonInScope()
        bind(LabelSpanConfig::class.java).toProvider(LabelSpanConfigProvider::class.java).providesSingletonInScope()
        bind(GitlabApi::class.java).toProvider(ApiProvider::class.java).providesSingletonInScope()
        bind(MarkDownConverter::class.java).toProvider(MarkDownConverterProvider::class.java).providesSingletonInScope()

        // Auth
        bind(OAuthParams::class.java).toInstance(
            OAuthParams(
                BuildConfig.OAUTH_APP_ID,
                BuildConfig.OAUTH_SECRET,
                BuildConfig.OAUTH_CALLBACK
            )
        )

        // Error handler with logout logic
        bind(ErrorHandler::class.java).singletonInScope()
    }
}