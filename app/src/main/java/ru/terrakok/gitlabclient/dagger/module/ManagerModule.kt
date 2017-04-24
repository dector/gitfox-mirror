package ru.terrakok.gitlabclient.dagger.module

import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.model.auth.AuthManager
import ru.terrakok.gitlabclient.model.profile.MyProfileManager
import ru.terrakok.gitlabclient.model.profile.ProfileRepository
import ru.terrakok.gitlabclient.model.project.MainProjectsListManager
import ru.terrakok.gitlabclient.model.project.ProjectRepository
import ru.terrakok.gitlabclient.model.server.ServerData
import javax.inject.Singleton

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 24.04.17
 */
@Module
class ManagerModule {

    @Provides
    @Singleton
    fun provideMyProfileManager(serverData: ServerData, authManager: AuthManager, profileRepository: ProfileRepository)
            = MyProfileManager(serverData, authManager, profileRepository)

    @Provides
    @Singleton
    fun provideMainProjectsListManager(projectRepository: ProjectRepository)
            = MainProjectsListManager(projectRepository)
}