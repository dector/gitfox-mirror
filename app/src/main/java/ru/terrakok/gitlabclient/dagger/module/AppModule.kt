package ru.terrakok.gitlabclient.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.model.auth.AuthData
import ru.terrakok.gitlabclient.model.resources.ResourceManager
import ru.terrakok.gitlabclient.model.server.GitlabApi
import ru.terrakok.gitlabclient.model.server.GitlabApiProvider
import ru.terrakok.gitlabclient.model.server.ServerConfig
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
    fun provideServerData() = ServerConfig()

    @Provides
    @Singleton
    fun provideAuthData(): AuthData = Prefs(context)

    @Provides
    @Singleton
    fun provideGitlabApi(authData: AuthData, serverConfig: ServerConfig): GitlabApi
            = GitlabApiProvider(authData, serverConfig, BuildConfig.DEBUG).api
}