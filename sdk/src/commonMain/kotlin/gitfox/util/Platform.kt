package gitfox.util

expect fun currentTimeMillis(): Long
expect fun randomUUID(): String
expect fun getQueryParameterFromUri(url: String, queryName: String): String