package ru.terrakok.gitlabclient.dagger.module

import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.model.data.auth.AuthHolder
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.ServerConfig
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Singleton

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.05.17
 */

@Module
class AuthModule {

    @Provides
    @Singleton
    fun provideAuthRepository(authData: AuthHolder, api: GitlabApi, schedulers: SchedulersProvider) =
            AuthRepository(authData, api, schedulers)

    @Provides
    @Singleton
    fun provideAuthInteractor(serverConfig: ServerConfig, tokenRepository: AuthRepository)
            = AuthInteractor(serverConfig, tokenRepository)
}