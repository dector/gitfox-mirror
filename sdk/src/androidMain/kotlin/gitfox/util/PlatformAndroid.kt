package gitfox.util

import android.content.Context
import gitfox.util.Tls12SocketFactory.Companion.enableTls12
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.Cache
import java.util.concurrent.TimeUnit

internal actual fun currentTimeMillis(): Long = System.currentTimeMillis()

internal fun createHttpEngine(
    context: Context,
    cacheSize: Long,
    timeout: Long
): HttpClientEngine = OkHttp.create {
    config {
        followRedirects(true)
        followSslRedirects(true)
        retryOnConnectionFailure(true)
        cache(Cache(context.cacheDir, cacheSize))
        connectTimeout(timeout, TimeUnit.SECONDS)
        readTimeout(timeout, TimeUnit.SECONDS)
        enableTls12()
    }
}
