package ru.terrakok.gitlabclient.di.provider

import gitfox.client.HttpClientFactory
import gitfox.model.data.server.UserAccountApi
import ru.terrakok.gitlabclient.BuildConfig
import javax.inject.Inject
import javax.inject.Provider

class UserAccountApiProvider @Inject constructor(
    private val httpClientFactory: HttpClientFactory
) : Provider<UserAccountApi> {
    override fun get() = UserAccountApi(httpClientFactory.create(null, BuildConfig.DEBUG))
}
