package ru.terrakok.gitlabclient.model.repository.profile

import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.toothpick.qualifier.ServerPath
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */
class ProfileRepository @Inject constructor(
        @ServerPath private val serverPath: String,
        private val api: GitlabApi,
        private val schedulers: SchedulersProvider
) {

    fun getMyProfile() = api
            .getMyUser()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getMyServerName() = serverPath
}