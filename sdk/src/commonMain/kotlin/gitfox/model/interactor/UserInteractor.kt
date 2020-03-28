package gitfox.model.interactor

import gitfox.entity.User
import gitfox.model.data.server.GitlabApi

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
class UserInteractor internal constructor(
    private val api: GitlabApi
) {

    suspend fun getUser(id: Long): User = api.getUser(id)
}
