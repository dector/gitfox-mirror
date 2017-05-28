package ru.terrakok.gitlabclient.dagger.module

import dagger.Module
import dagger.Provides
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.ServerConfig
import ru.terrakok.gitlabclient.model.interactor.profile.MyProfileInteractor
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository
import ru.terrakok.gitlabclient.model.repository.profile.ProfileRepository
import ru.terrakok.gitlabclient.model.system.SchedulersProvider

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 24.04.17
 */
@Module
class ProfileModule {

    @Provides
    fun provideProfileRepository(serverConfig: ServerConfig, api: GitlabApi, schedulers: SchedulersProvider) =
            ProfileRepository(serverConfig, api, schedulers)

    @Provides
    fun provideMyProfileInteractor(authRepository: AuthRepository, profileRepository: ProfileRepository)
            = MyProfileInteractor(authRepository, profileRepository)
}