package ru.terrakok.gitlabclient.model.data.server

import okhttp3.Headers

class RequestHeadersReader {

    companion object {
        private const val X_TOTAL = "X-Total"
    }

    fun getXTotal(headers: Headers) = headers.get(X_TOTAL)?.toInt() ?: 0
}