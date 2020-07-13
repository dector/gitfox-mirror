package gitfox.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js
import kotlin.js.Date

internal actual fun currentTimeMillis(): Long = Date().getTime().toLong()

internal fun createHttpEngine(
    cacheSize: Long,
    timeout: Long
): HttpClientEngine = Js.create()
