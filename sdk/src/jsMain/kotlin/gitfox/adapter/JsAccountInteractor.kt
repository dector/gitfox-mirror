package gitfox.adapter

import gitfox.model.interactor.AccountInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

class JsAccountInteractor internal constructor(
    private val interactor: AccountInteractor
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    @JsName("getAccountMainBadges")
    fun getAccountMainBadges() = interactor.getAccountMainBadges().wrap()

    @JsName("getMyProfile")
    fun getMyProfile() = promise { interactor.getMyProfile() }

    @JsName("getMyServerName")
    fun getMyServerName(): String = interactor.getMyServerName()

    @JsName("getMyTodos")
    fun getMyTodos(
        isPending: Boolean,
        page: Int
    ) = promise {
        interactor.getMyTodos(isPending, page)
    }

    @JsName("getMyMergeRequests")
    fun getMyMergeRequests(
        createdByMe: Boolean,
        onlyOpened: Boolean,
        page: Int
    ) = promise {
        interactor.getMyMergeRequests(createdByMe, onlyOpened, page)
    }

    @JsName("getMyIssues")
    fun getMyIssues(
        createdByMe: Boolean,
        onlyOpened: Boolean,
        page: Int
    ) = promise {
        interactor.getMyIssues(createdByMe, onlyOpened, page)
    }
}