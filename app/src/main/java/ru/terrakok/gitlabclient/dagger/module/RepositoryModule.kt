package ru.terrakok.gitlabclient.dagger.module

import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.model.auth.TokenRepository
import ru.terrakok.gitlabclient.model.profile.ProfileRepository
import ru.terrakok.gitlabclient.model.project.ProjectRepository
import ru.terrakok.gitlabclient.model.server.GitlabApi
import javax.inject.Singleton

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideProfileRepository(api: GitlabApi) = ProfileRepository(api)

    @Provides
    @Singleton
    fun provideTokenRepository(api: GitlabApi) = TokenRepository(api)

    @Provides
    @Singleton
    fun provideProjectRepository(api: GitlabApi) = ProjectRepository(api)
}