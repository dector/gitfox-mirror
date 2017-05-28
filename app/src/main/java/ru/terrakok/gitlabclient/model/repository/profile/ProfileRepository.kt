package ru.terrakok.gitlabclient.model.repository.profile

import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.ServerConfig
import ru.terrakok.gitlabclient.model.system.SchedulersProvider

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */
class ProfileRepository(private val serverConfig: ServerConfig,
                        private val api: GitlabApi,
                        private val schedulers: SchedulersProvider) {

    fun getMyProfile() =
            api.getMyUser()
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())

    fun getMyServerName() = serverConfig.SERVER_URL
}