package gitfox.util

internal expect fun currentTimeMillis(): Long
internal expect fun getQueryParameterFromUri(url: String, queryName: String): String