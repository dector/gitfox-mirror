package ru.terrakok.gitlabclient.di.provider

import gitfox.client.HttpClientFactory
import gitfox.entity.app.session.AuthHolder
import gitfox.model.data.cache.ProjectCache
import gitfox.model.data.server.ApiWithChangesRegistration
import gitfox.model.data.server.ApiWithProjectCache
import gitfox.model.data.server.GitlabApi
import gitfox.model.data.server.GitlabApiImpl
import gitfox.model.data.state.ServerChanges
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.di.ServerPath
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
