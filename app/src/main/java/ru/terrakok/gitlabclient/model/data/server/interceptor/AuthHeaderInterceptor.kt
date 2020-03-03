package ru.terrakok.gitlabclient.model.data.server.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import ru.terrakok.gitlabclient.entity.app.session.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.TokenInvalidError

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 23.04.17.
 */
class AuthHeaderInterceptor(private val authData: AuthHolder) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val token = authData.token
        if (token != null) {
            try {
                request = if (authData.isOAuth) {
                    request.newBuilder().addHeader("Authorization", "Bearer $token").build()
                } else {
                    request.newBuilder().addHeader("PRIVATE-TOKEN", token).build()
                }
            } catch (e: IllegalArgumentException) {
                // If token can't be parsed, just logout user to change it with correct value.
                throw TokenInvalidError()
            }
        }
        return chain.proceed(request)
    }
}
