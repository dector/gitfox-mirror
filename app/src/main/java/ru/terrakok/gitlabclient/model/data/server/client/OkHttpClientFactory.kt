package ru.terrakok.gitlabclient.model.data.server.client

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.terrakok.gitlabclient.entity.app.session.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.client.Tls12SocketFactory.Companion.enableTls12
import ru.terrakok.gitlabclient.model.data.server.interceptor.AuthHeaderInterceptor
import ru.terrakok.gitlabclient.model.data.server.interceptor.CurlLoggingInterceptor
import ru.terrakok.gitlabclient.model.data.server.interceptor.ErrorResponseInterceptor
import java.util.concurrent.TimeUnit

class OkHttpClientFactory(
    private val context: Context
) {

    fun create(
        authData: AuthHolder?,
        enableErrorHandler: Boolean,
        enableLogging: Boolean
    ): OkHttpClient =
        OkHttpClient.Builder().apply {
            followRedirects(true)
            followSslRedirects(true)
            retryOnConnectionFailure(true)
            cache(Cache(context.cacheDir, CACHE_SIZE_BYTES))
            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            enableTls12()

            authData?.let { addNetworkInterceptor(AuthHeaderInterceptor(authData)) }
            if (enableLogging) {
                addNetworkInterceptor(
                    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
                )
                addNetworkInterceptor(CurlLoggingInterceptor())
            }
            if (enableErrorHandler) {
                addNetworkInterceptor(ErrorResponseInterceptor())
            }
        }.build()

    companion object {
        private const val CACHE_SIZE_BYTES = 20 * 1024L
        private const val TIMEOUT = 30L
    }
}
