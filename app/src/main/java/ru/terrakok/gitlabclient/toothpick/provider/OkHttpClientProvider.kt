package ru.terrakok.gitlabclient.toothpick.provider

import okhttp3.OkHttpClient
import ru.terrakok.gitlabclient.model.data.server.interceptor.ErrorResponseInterceptor
import javax.inject.Inject
import javax.inject.Provider

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 20.06.17.
 */
class OkHttpClientProvider @Inject constructor(
    private val baseOkHttpProvider: BaseAuthOkHttpProvider
) : Provider<OkHttpClient> {

    override fun get() = baseOkHttpProvider.get()
        .newBuilder()
        .addNetworkInterceptor(ErrorResponseInterceptor())
        .build()
}