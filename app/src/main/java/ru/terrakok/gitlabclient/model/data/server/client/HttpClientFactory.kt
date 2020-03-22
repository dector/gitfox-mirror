package ru.terrakok.gitlabclient.model.data.server.client

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.util.KtorExperimentalAPI
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.OkHttpClient
import ru.terrakok.gitlabclient.entity.app.session.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.client.Tls12SocketFactory.Companion.enableTls12
import ru.terrakok.gitlabclient.model.data.server.interceptor.AuthHeaderInterceptor
import ru.terrakok.gitlabclient.model.data.server.interceptor.ErrorResponseInterceptor
import java.util.concurrent.TimeUnit

class HttpClientFactory(
    private val context: Context,
    private val json: Json
) {

    @OptIn(KtorExperimentalAPI::class)
    fun create(
        authData: AuthHolder?,
        enableLogging: Boolean
    ): HttpClient = HttpClient(OkHttp) {
        engine {
            preconfigured = createOkHttp(authData, enableLogging)
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }
    }

    fun createOkHttp(
        authData: AuthHolder?,
        enableLogging: Boolean
    ) = OkHttpClient.Builder().apply {
        followRedirects(true)
        followSslRedirects(true)
        retryOnConnectionFailure(true)
        cache(Cache(context.cacheDir, CACHE_SIZE_BYTES))
        connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        readTimeout(TIMEOUT, TimeUnit.SECONDS)
        enableTls12()

        authData?.let { addNetworkInterceptor(AuthHeaderInterceptor(authData)) }
        addNetworkInterceptor(ErrorResponseInterceptor())
    }.build()

    companion object {
        private const val CACHE_SIZE_BYTES = 20 * 1024L
        private const val TIMEOUT = 30L
    }
}
