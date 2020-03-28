package gitfox.util

internal expect fun currentTimeMillis(): Long
internal expect fun randomUUID(): String
internal expect fun getQueryParameterFromUri(url: String, queryName: String): String