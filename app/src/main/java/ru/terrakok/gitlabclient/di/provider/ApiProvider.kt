package ru.terrakok.gitlabclient.di.provider

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.terrakok.gitlabclient.di.ServerPath
import ru.terrakok.gitlabclient.di.WithErrorHandler
import ru.terrakok.gitlabclient.model.data.cache.ProjectCache
import ru.terrakok.gitlabclient.model.data.server.ApiWithCache
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import javax.inject.Inject
import javax.inject.Provider

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.06.17.
 */
class ApiProvider @Inject constructor(
    @WithErrorHandler private val okHttpClient: OkHttpClient,
    private val gson: Gson,
    private val projectCache: ProjectCache,
    @ServerPath private val serverPath: String
) : Provider<GitlabApi> {

    override fun get() =
        ApiWithCache(
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(serverPath)
                .build()
                .create(GitlabApi::class.java),
            projectCache
        )
}