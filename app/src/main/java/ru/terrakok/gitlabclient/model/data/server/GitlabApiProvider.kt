package ru.terrakok.gitlabclient.model.data.server

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
class GitlabApiProvider(okHttpClient: OkHttpClient, gson: Gson, serverConfig: ServerConfig) {

    val api: GitlabApi

    init {
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(serverConfig.SERVER_URL).build()

        api = retrofit.create(GitlabApi::class.java)
    }
}