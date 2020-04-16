package gitfox.adapter

import gitfox.entity.User
import gitfox.entity.app.target.TargetHeader
import gitfox.model.interactor.AccountInteractor
import kotlinx.coroutines.CoroutineScope

class IosAccountInteractor internal constructor(
    private val interactor: AccountInteractor
) : CoroutineScope by CoroutineScope(MainLoopDispatcher) {

    fun getAccountMainBadges() = interactor.getAccountMainBadges().wrap()

    fun getMyProfile(callback: (result: User?, error: Exception?) -> Unit) {
        wrap(callback) { interactor.getMyProfile() }
    }

    fun getMyServerName(): String = interactor.getMyServerName()

    fun getMyTodos(
        isPending: Boolean,
        page: Int,
        callback: (result: List<TargetHeader>?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.getMyTodos(isPending, page) }
    }

    fun getMyMergeRequests(
        createdByMe: Boolean,
        onlyOpened: Boolean,
        page: Int,
        callback: (result: List<TargetHeader>?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.getMyMergeRequests(createdByMe, onlyOpened, page) }
    }

    fun getMyIssues(
        createdByMe: Boolean,
        onlyOpened: Boolean,
        page: Int,
        callback: (result: List<TargetHeader>?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.getMyIssues(createdByMe, onlyOpened, page) }
    }
}