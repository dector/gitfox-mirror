package ru.terrakok.gitlabclient.di.provider

import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.di.ServerPath
import ru.terrakok.gitlabclient.entity.app.session.AuthHolder
import ru.terrakok.gitlabclient.model.data.cache.ProjectCache
import ru.terrakok.gitlabclient.model.data.server.ApiWithChangesRegistration
import ru.terrakok.gitlabclient.model.data.server.ApiWithProjectCache
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.GitlabApiImpl
import ru.terrakok.gitlabclient.model.data.server.client.HttpClientFactory
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
import javax.inject.Inject
import javax.inject.Provider

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.06.17.
 */
class ApiProvider @Inject constructor(
        private val httpClientFactory: HttpClientFactory,
        private val authHolder: AuthHolder,
        private val projectCache: ProjectCache,
        private val serverChanges: ServerChanges,
        @ServerPath private val serverPath: String
) : Provider<GitlabApi> {

    override fun get() =
        ApiWithChangesRegistration(
            ApiWithProjectCache(
                GitlabApiImpl(serverPath, httpClientFactory.create(authHolder, BuildConfig.DEBUG)),
                projectCache
            ),
            serverChanges
        )

}
