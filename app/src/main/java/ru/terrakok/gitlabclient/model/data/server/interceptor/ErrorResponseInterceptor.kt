package ru.terrakok.gitlabclient.model.data.server.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.terrakok.gitlabclient.model.data.server.ServerError

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
class ErrorResponseInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        val code = response.code
        if (code in 400..500) throw ServerError(code)

        return response
    }
}