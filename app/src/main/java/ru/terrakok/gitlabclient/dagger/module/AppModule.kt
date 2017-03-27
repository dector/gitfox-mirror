package ru.terrakok.gitlabclient.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.model.auth.AuthManager
import ru.terrakok.gitlabclient.model.resources.ResourceManager
import ru.terrakok.gitlabclient.model.server.ServerConfig
import javax.inject.Singleton

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 27.03.17
 */
@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideAuthManager() = AuthManager()

    @Provides
    @Singleton
    fun provideServerConfig() = ServerConfig()

    @Provides
    @Singleton
    fun provideResourceManager() = ResourceManager(context)
}