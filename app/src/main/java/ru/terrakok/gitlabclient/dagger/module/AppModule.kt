package ru.terrakok.gitlabclient.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.model.auth.AuthManager
import ru.terrakok.gitlabclient.model.resources.ResourceManager
import ru.terrakok.gitlabclient.model.server.ServerManager
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
    fun providePrefs() = Prefs(context)

    @Provides
    @Singleton
    fun provideAuthManager(prefs: Prefs) = AuthManager(prefs)

    @Provides
    @Singleton
    fun provideServerManager(authManager: AuthManager) = ServerManager(authManager, BuildConfig.DEBUG)
}