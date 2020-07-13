package gitfox.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.ios.Ios
import kotlin.system.getTimeMillis

internal actual fun currentTimeMillis(): Long = getTimeMillis()

internal fun createHttpEngine(
    cacheSize: Long,
    timeout: Long
): HttpClientEngine = Ios.create {
    configureRequest {
        setAllowsCellularAccess(true)
    }
}
