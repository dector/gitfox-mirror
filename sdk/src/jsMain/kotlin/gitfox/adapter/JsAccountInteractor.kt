package gitfox.adapter

import gitfox.model.interactor.AccountInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

class JsAccountInteractor internal constructor(
    private val interactor: AccountInteractor
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    fun getAccountMainBadges() = interactor.getAccountMainBadges().wrap()

    fun getMyProfile() = promise { interactor.getMyProfile() }

    fun getMyServerName(): String = interactor.getMyServerName()

    fun getMyTodos(
        isPending: Boolean,
        page: Int
    ) = promise {
        interactor.getMyTodos(isPending, page)
    }

    fun getMyMergeRequests(
        createdByMe: Boolean,
        onlyOpened: Boolean,
        page: Int
    ) = promise {
        interactor.getMyMergeRequests(createdByMe, onlyOpened, page)
    }

    fun getMyIssues(
        createdByMe: Boolean,
        onlyOpened: Boolean,
        page: Int
    ) = promise {
        interactor.getMyIssues(createdByMe, onlyOpened, page)
    }
}