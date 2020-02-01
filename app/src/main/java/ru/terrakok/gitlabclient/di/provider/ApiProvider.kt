package ru.terrakok.gitlabclient.di.provider

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.di.ServerPath
import ru.terrakok.gitlabclient.entity.app.session.AuthHolder
import ru.terrakok.gitlabclient.model.data.cache.ProjectCache
import ru.terrakok.gitlabclient.model.data.server.ApiWithChangesRegistration
import ru.terrakok.gitlabclient.model.data.server.ApiWithProjectCache
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.client.OkHttpClientFactory
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
import javax.inject.Inject
import javax.inject.Provider

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.06.17.
 */
class ApiProvider @Inject constructor(
    private val okHttpClientFactory: OkHttpClientFactory,
    private val authHolder: AuthHolder,
    private val gson: Gson,
    private val projectCache: ProjectCache,
    private val serverChanges: ServerChanges,
    @ServerPath private val serverPath: String
) : Provider<GitlabApi> {

    override fun get() =
        ApiWithChangesRegistration(
            ApiWithProjectCache(
                getOriginalApi(),
                projectCache
            ),
            serverChanges
        )

    private fun getOriginalApi() =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClientFactory.create(authHolder, true, BuildConfig.DEBUG))
            .baseUrl(serverPath)
            .build()
            .create(GitlabApi::class.java)
}
