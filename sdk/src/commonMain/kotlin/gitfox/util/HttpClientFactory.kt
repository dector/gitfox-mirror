package gitfox.util

import gitfox.entity.app.session.AuthHolder
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

interface HttpClientFactory {
    fun create(
        json: Json,
        authData: AuthHolder?,
        enableLogging: Boolean
    ): HttpClient
}