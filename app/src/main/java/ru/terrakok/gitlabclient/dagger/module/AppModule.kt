package ru.terrakok.gitlabclient.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.model.auth.AuthData
import ru.terrakok.gitlabclient.model.auth.AuthManager
import ru.terrakok.gitlabclient.model.auth.TokenRepository
import ru.terrakok.gitlabclient.model.resources.ResourceManager
import ru.terrakok.gitlabclient.model.server.GitlabApi
import ru.terrakok.gitlabclient.model.server.GitlabApiProvider
import ru.terrakok.gitlabclient.model.server.ServerData
import ru.terrakok.gitlabclient.model.storage.Prefs
import javax.inject.Singleton

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideResourceManager() = ResourceManager(context)

    @Provides
    @Singleton
    fun provideServerData() = ServerData()

    @Provides
    @Singleton
    fun provideAuthData(): AuthData = Prefs(context)

    @Provides
    @Singleton
    fun provideGitlabApi(authData: AuthData, serverData: ServerData): GitlabApi
            = GitlabApiProvider(authData, serverData, BuildConfig.DEBUG).api

    @Provides
    @Singleton
    fun provideAuthManager(authData: AuthData, serverData: ServerData, tokenRepository: TokenRepository)
            = AuthManager(authData, serverData, tokenRepository)
}