package ru.terrakok.gitlabclient.model.interactor.profile

import io.reactivex.Observable
import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.app.MyUserInfo
import ru.terrakok.gitlabclient.model.repository.auth.AuthRepository
import ru.terrakok.gitlabclient.model.repository.profile.ProfileRepository

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */

class MyProfileInteractor(
        private val authRepository: AuthRepository,
        private val profileRepository: ProfileRepository) {

    fun getMyProfile(): Observable<MyUserInfo> =
            authRepository.getSignState()
                    .flatMapSingle {
                        if (it) {
                            profileRepository
                                    .getMyProfile()
                                    .map { MyUserInfo(it, profileRepository.getMyServerName()) }
                        } else {
                            Single.just(MyUserInfo(null, profileRepository.getMyServerName()))
                        }
                    }

}