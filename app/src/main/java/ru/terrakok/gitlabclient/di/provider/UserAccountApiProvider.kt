package ru.terrakok.gitlabclient.di.provider

import kotlinx.serialization.json.Json
import ru.terrakok.gitlabclient.model.data.server.UserAccountApi
import ru.terrakok.gitlabclient.model.data.server.client.OkHttpClientFactory
import javax.inject.Inject
import javax.inject.Provider

class UserAccountApiProvider @Inject constructor(
        private val json: Json,
        private val okHttpClientFactory: OkHttpClientFactory
) : Provider<UserAccountApi> {
    override fun get() = UserAccountApi(json, okHttpClientFactory)
}
