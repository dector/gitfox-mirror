package gitfox.util

import java.net.URI
import java.util.*

internal actual fun currentTimeMillis(): Long = System.currentTimeMillis()
internal actual fun randomUUID(): String = UUID.randomUUID().toString()

internal actual fun getQueryParameterFromUri(url: String, queryName: String): String {
    val uri = URI(url)
    val query = uri.query
    val parameters = query.split("&")

    var code = ""
    for (parameter in parameters) {
        if (parameter.startsWith(queryName)) {
            code = parameter.substring(queryName.length + 1)
            break
        }
    }
    return code
}