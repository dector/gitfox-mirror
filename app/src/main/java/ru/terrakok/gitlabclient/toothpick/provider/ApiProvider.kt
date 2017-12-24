package ru.terrakok.gitlabclient.toothpick.provider

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.toothpick.qualifier.ServerPath
import javax.inject.Inject
import javax.inject.Provider

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.06.17.
 */
class ApiProvider @Inject constructor(
        private val okHttpClient: OkHttpClient,
        private val gson: Gson,
        @ServerPath private val serverPath: String
) : Provider<GitlabApi> {

    override fun get() =
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .baseUrl(serverPath)
                    .build()
                    .create(GitlabApi::class.java)
}