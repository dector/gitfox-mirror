package ru.terrakok.gitlabclient.model.profile

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.gitlabclient.model.server.GitlabApi
import ru.terrakok.gitlabclient.model.server.ServerConfig

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */
class ProfileRepository(
        private val serverConfig: ServerConfig,
        private val api: GitlabApi) {

    fun getMyProfile() =
            api.getMyUser()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    fun getMyServerName() = serverConfig.SERVER_URL
}