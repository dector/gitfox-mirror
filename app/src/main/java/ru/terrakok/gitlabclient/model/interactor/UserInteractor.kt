package ru.terrakok.gitlabclient.model.interactor

import ru.terrakok.gitlabclient.entity.User
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
class UserInteractor @Inject constructor(
    private val api: GitlabApi
) {

    suspend fun getUser(id: Long): User = api.getUser(id)
}
