package ru.terrakok.gitlabclient.dagger.module

import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.model.auth.AuthData
import ru.terrakok.gitlabclient.model.auth.AuthRepository
import ru.terrakok.gitlabclient.model.profile.ProfileRepository
import ru.terrakok.gitlabclient.model.project.ProjectRepository
import ru.terrakok.gitlabclient.model.server.GitlabApi
import ru.terrakok.gitlabclient.model.server.ServerConfig
import javax.inject.Singleton

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */
@Module
class RepositoryModule {

    @Provides
    fun provideProfileRepository(serverConfig: ServerConfig, api: GitlabApi) =
            ProfileRepository(serverConfig, api)

    @Provides
    @Singleton
    fun provideAuthRepository(authData: AuthData, api: GitlabApi) =
            AuthRepository(authData, api)

    @Provides
    fun provideProjectRepository(api: GitlabApi) =
            ProjectRepository(api)
}