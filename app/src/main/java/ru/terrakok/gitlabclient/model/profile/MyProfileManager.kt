package ru.terrakok.gitlabclient.model.profile

import io.reactivex.Observable
import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.model.auth.AuthRepository

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */

class MyProfileManager(
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

data class MyUserInfo(val user: User?, val serverName: String)