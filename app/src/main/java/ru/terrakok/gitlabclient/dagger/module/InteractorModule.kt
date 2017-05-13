package ru.terrakok.gitlabclient.dagger.module

import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.model.data.server.ServerConfig
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.model.interactor.profile.MyProfileInteractor
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.model.interactor.projects.MainProjectsListInteractor
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository
import ru.terrakok.gitlabclient.model.repository.profile.ProfileRepository
import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository
import javax.inject.Singleton

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 24.04.17
 */
@Module
class InteractorModule {

    @Provides
    fun provideMyProfileInteractor(authRepository: AuthRepository, profileRepository: ProfileRepository)
            = MyProfileInteractor(authRepository, profileRepository)

    @Provides
    fun provideMainProjectsListInteractor(projectRepository: ProjectRepository)
            = MainProjectsListInteractor(projectRepository)

    @Provides
    @Singleton
    fun provideAuthInteractor(serverConfig: ServerConfig, tokenRepository: AuthRepository)
            = AuthInteractor(serverConfig, tokenRepository)

    @Provides
    fun provideProjectInteractor(projectRepository: ProjectRepository)
            = ProjectInteractor(projectRepository)
}