package gitfox.adapter

import gitfox.model.interactor.UserInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

class JsUserInteractor internal constructor(
    private val interactor: UserInteractor
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    fun getUser(id: Long) = promise {
        interactor.getUser(id)
    }
}