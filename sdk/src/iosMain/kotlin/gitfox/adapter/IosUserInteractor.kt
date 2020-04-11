package gitfox.adapter

import gitfox.entity.User
import gitfox.model.interactor.UserInteractor
import kotlinx.coroutines.CoroutineScope

class IosUserInteractor internal constructor(
    private val interactor: UserInteractor
) : CoroutineScope by CoroutineScope(MainLoopDispatcher) {

    fun getUser(id: Long, callback: (result: User?, error: Exception?) -> Unit) {
        fire(callback) { interactor.getUser(id) }
    }
}