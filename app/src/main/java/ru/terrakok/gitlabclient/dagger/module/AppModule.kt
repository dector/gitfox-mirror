package ru.terrakok.gitlabclient.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.GitlabApiProvider
import ru.terrakok.gitlabclient.model.data.server.ServerConfig
import ru.terrakok.gitlabclient.model.data.storage.Prefs
import ru.terrakok.gitlabclient.model.manager.ResourceManager
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
    fun provideAuthData(): AuthHolder = Prefs(context)

    @Provides
    @Singleton
    fun provideGitlabApi(authData: AuthHolder, serverConfig: ServerConfig): GitlabApi
            = GitlabApiProvider(authData, serverConfig, BuildConfig.DEBUG).api
}