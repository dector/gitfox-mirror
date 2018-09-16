package ru.terrakok.gitlabclient.toothpick.provider

import android.content.Context
import okhttp3.Cache
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
class OkHttpClientProvider @Inject constructor(
    private val authData: AuthHolder,
    private val context: Context
) : Provider<OkHttpClient> {

    override fun get() = with(OkHttpClient.Builder()) {
        cache(Cache(context.cacheDir, 20 * 1024))
        connectTimeout(30, TimeUnit.SECONDS)
        readTimeout(30, TimeUnit.SECONDS)

        addNetworkInterceptor(AuthHeaderInterceptor(authData))
        addNetworkInterceptor(ErrorResponseInterceptor())
        if (BuildConfig.DEBUG) {
            addNetworkInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
            addNetworkInterceptor(CurlLoggingInterceptor())
        }
        build()
    }
}