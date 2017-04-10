package ru.terrakok.gitlabclient.dagger.module

import android.content.Context
import dagger.Lazy
import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.BuildConfig
import ru.terrakok.gitlabclient.model.profile.ProfileManager
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
    fun provideProfileManager(prefs: Prefs, serverManager: Lazy<ServerManager>, router: Router)
            = ProfileManager(prefs, serverManager, router)

    @Provides
    @Singleton
    fun provideServerManager(profileManager: ProfileManager)
            = ServerManager(profileManager, BuildConfig.DEBUG)
}