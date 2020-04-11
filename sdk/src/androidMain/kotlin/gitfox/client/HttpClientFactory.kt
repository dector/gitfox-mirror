package gitfox.client

import android.content.Context
import com.github.aakira.napier.Napier
import gitfox.client.Tls12SocketFactory.Companion.enableTls12
import gitfox.entity.app.session.AuthHolder
import gitfox.util.HttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.auth.Auth
import io.ktor.client.features.auth.AuthProvider
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.auth.HttpAuthHeader
import kotlinx.serialization.json.Json
import okhttp3.Cache
import java.util.concurrent.TimeUnit

internal class HttpClientFactory(
    private val context: Context
) : HttpClientFactory {

    override fun create(
        json: Json,
        authData: AuthHolder?,
        enableLogging: Boolean
    ): HttpClient = HttpClient(OkHttp) {
        engine {
            config {
                followRedirects(true)
                followSslRedirects(true)
                retryOnConnectionFailure(true)
                cache(Cache(context.cacheDir, CACHE_SIZE_BYTES))
                connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                readTimeout(TIMEOUT, TimeUnit.SECONDS)
                enableTls12()
            }
        }
        if (enableLogging) {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        Napier.v(tag = "HttpClient", message = message)
                    }
                }
            }
        }
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }
        install(Auth) {
            providers.add(object : AuthProvider {
                override val sendWithoutRequest = true
                override fun isApplicable(auth: HttpAuthHeader) = true

                override suspend fun addRequestHeaders(request: HttpRequestBuilder) {
                    authData?.let {
                        if (authData.isOAuth) {
                            request.headers["Authorization"] = "Bearer ${authData.token}"
                        } else {
                            request.headers["PRIVATE-TOKEN"] = authData.token.toString()
                        }
                    }
                }

            })
        }
    }

    companion object {
        private const val CACHE_SIZE_BYTES = 20 * 1024L
        private const val TIMEOUT = 30L
    }
}
