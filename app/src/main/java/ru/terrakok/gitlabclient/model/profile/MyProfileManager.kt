package ru.terrakok.gitlabclient.model.profile

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.model.auth.AuthManager
import ru.terrakok.gitlabclient.model.server.ServerData

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */

class MyProfileManager(
        private val serverData: ServerData,
        private val authManager: AuthManager,
        private val profileRepository: ProfileRepository) {

    fun getMyProfile(): Observable<User> =
            authManager.getSignState()
                    .filter { it }
                    .flatMapSingle {
                        profileRepository.getMyProfile()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                    }

    fun getMyServerName() = serverData.SERVER_URL
}