package ru.terrakok.gitlabclient.dagger.module

import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.model.auth.AuthManager
import ru.terrakok.gitlabclient.model.auth.AuthRepository
import ru.terrakok.gitlabclient.model.profile.MyProfileManager
import ru.terrakok.gitlabclient.model.profile.ProfileRepository
import ru.terrakok.gitlabclient.model.project.MainProjectsListManager
import ru.terrakok.gitlabclient.model.project.ProjectManager
import ru.terrakok.gitlabclient.model.project.ProjectRepository
import ru.terrakok.gitlabclient.model.server.ServerConfig
import javax.inject.Singleton

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 24.04.17
 */
@Module
class ManagerModule {

    @Provides
    fun provideMyProfileManager(authRepository: AuthRepository, profileRepository: ProfileRepository)
            = MyProfileManager(authRepository, profileRepository)

    @Provides
    fun provideMainProjectsListManager(projectRepository: ProjectRepository)
            = MainProjectsListManager(projectRepository)

    @Provides
    @Singleton
    fun provideAuthManager(serverConfig: ServerConfig, tokenRepository: AuthRepository)
            = AuthManager(serverConfig, tokenRepository)

    @Provides
    fun provideProjectManager(projectRepository: ProjectRepository)
            = ProjectManager(projectRepository)
}