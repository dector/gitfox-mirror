package ru.terrakok.gitlabclient.toothpick.provider

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.interceptor.AuthHeaderInterceptor
import ru.terrakok.gitlabclient.model.data.server.interceptor.CurlLoggingInterceptor
import ru.terrakok.gitlabclient.model.data.server.interceptor.ErrorResponseInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.06.17.
 */
class OkHttpClientProvider @Inject constructor(authData: AuthHolder) : Provider<OkHttpClient> {
    private val httpClient: OkHttpClient

    init {
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.addNetworkInterceptor(AuthHeaderInterceptor(authData))
        httpClientBuilder.addNetworkInterceptor(ErrorResponseInterceptor())
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            httpClientBuilder.addNetworkInterceptor(httpLoggingInterceptor)
            httpClientBuilder.addNetworkInterceptor(CurlLoggingInterceptor())
        }
        httpClient = httpClientBuilder.build()
    }

    override fun get() = httpClient
}