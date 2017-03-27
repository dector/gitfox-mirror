package ru.terrakok.gitlabclient.dagger.module

import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.model.auth.AuthManager
import ru.terrakok.gitlabclient.model.server.ServerConfig
import javax.inject.Singleton

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideAuthManager() = AuthManager()

    @Provides
    @Singleton
    fun provideServerConfig() = ServerConfig()
}