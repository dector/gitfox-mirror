package ru.terrakok.gitlabclient.di.provider

import javax.inject.Inject
import javax.inject.Provider
import okhttp3.OkHttpClient
import ru.terrakok.gitlabclient.model.data.server.interceptor.ErrorResponseInterceptor

/**
 * @author Myalkin Maxim (@MaxMyalkin) on 29.10.18.
 */
class OkHttpClientWithErrorHandlerProvider @Inject constructor(
    private val client: OkHttpClient
) : Provider<OkHttpClient> {

    override fun get() = client
        .newBuilder()
        .addNetworkInterceptor(ErrorResponseInterceptor())
        .build()
}