package ru.terrakok.gitlabclient.dagger.module

import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.ServerConfig
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository
import ru.terrakok.gitlabclient.model.repository.profile.ProfileRepository
import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository
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
    fun provideAuthRepository(authData: AuthHolder, api: GitlabApi) =
            AuthRepository(authData, api)

    @Provides
    fun provideProjectRepository(api: GitlabApi) = ProjectRepository(api)
}