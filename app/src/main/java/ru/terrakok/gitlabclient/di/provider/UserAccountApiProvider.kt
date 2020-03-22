package ru.terrakok.gitlabclient.di.provider

import ru.terrakok.gitlabclient.model.data.server.UserAccountApi
import ru.terrakok.gitlabclient.model.data.server.client.HttpClientFactory
import javax.inject.Inject
import javax.inject.Provider

class UserAccountApiProvider @Inject constructor(
        private val httpClientFactory: HttpClientFactory
) : Provider<UserAccountApi> {
    override fun get() = UserAccountApi(httpClientFactory)
}
