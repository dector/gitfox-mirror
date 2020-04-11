package gitfox.client

import com.github.aakira.napier.Napier
import gitfox.entity.app.session.AuthHolder
import gitfox.util.HttpClientFactory
import io.ktor.client.HttpClient
import io.ktor.client.engine.ios.Ios
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

internal class HttpClientFactory : HttpClientFactory {
    override fun create(
        json: Json,
        authData: AuthHolder?,
        enableLogging: Boolean
    ): HttpClient = HttpClient(Ios) {
        engine {
            configureRequest {
                setAllowsCellularAccess(true)
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
