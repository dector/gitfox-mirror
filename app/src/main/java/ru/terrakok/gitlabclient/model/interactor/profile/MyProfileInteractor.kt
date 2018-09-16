package ru.terrakok.gitlabclient.model.interactor.profile

import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.app.user.MyUserInfo
import ru.terrakok.gitlabclient.model.repository.profile.ProfileRepository
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */

class MyProfileInteractor @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    fun getMyProfile(): Single<MyUserInfo> =
        profileRepository
            .getMyProfile()
            .map { MyUserInfo(it, profileRepository.getMyServerName()) }

}