package ru.terrakok.gitlabclient.toothpick.provider

import com.google.gson.Gson
import okhttp3.OkHttpClient
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.GitlabApiProvider
import ru.terrakok.gitlabclient.model.data.server.ServerConfig
import javax.inject.Inject
import javax.inject.Provider

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.06.17.
 */
class ApiProvider @Inject constructor(okHttpClient: OkHttpClient,
                                      gson: Gson,
                                      serverConfig: ServerConfig) : Provider<GitlabApi> {

    private val apiHolder: GitlabApiProvider = GitlabApiProvider(okHttpClient, gson, serverConfig)

    override fun get() = apiHolder.api
}