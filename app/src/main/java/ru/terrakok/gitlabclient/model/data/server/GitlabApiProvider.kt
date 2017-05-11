package ru.terrakok.gitlabclient.model.data.server

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.interceptor.AuthHeaderInterceptor
import ru.terrakok.gitlabclient.model.data.server.interceptor.CurlLoggingInterceptor

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
class GitlabApiProvider(
        authData: AuthHolder,
        serverConfig: ServerConfig,
        debug: Boolean) {

    val api: GitlabApi

    init {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addNetworkInterceptor(AuthHeaderInterceptor(authData))

        if (debug) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addNetworkInterceptor(httpLoggingInterceptor)
            httpClientBuilder.addNetworkInterceptor(CurlLoggingInterceptor())
        }

        val gson = GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create()

        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClientBuilder.build())
                .baseUrl(serverConfig.SERVER_URL).build()

        api = retrofit.create(GitlabApi::class.java)
    }
}