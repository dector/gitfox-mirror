package gitfox.adapter

import gitfox.entity.IssueScope
import gitfox.entity.IssueState
import gitfox.entity.OrderBy
import gitfox.entity.Sort
import gitfox.entity.app.target.TargetHeader
import gitfox.model.interactor.IssueInteractor
import kotlinx.coroutines.CoroutineScope

class IosIssueInteractor internal constructor(
    private val interactor: IssueInteractor,
    private val defaultPageSize: Int
) : CoroutineScope by CoroutineScope(MainLoopDispatcher) {

    fun getMyIssues(
        scope: IssueScope? = null,
        state: IssueState? = null,
        labels: String? = null,
        milestone: String? = null,
        iids: Array<Long>? = null,
        orderBy: OrderBy? = OrderBy.UPDATED_AT,
        sort: Sort? = Sort.ASC,
        search: String? = null,
        page: Int,
        pageSize: Int = defaultPageSize,
        callback: (result: List<TargetHeader>?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.getMyIssues(scope, state, labels, milestone, iids, orderBy, sort, search, page, pageSize) }
    }

    fun getIssues(
        projectId: Long,
        scope: IssueScope? = null,
        state: IssueState? = null,
        labels: String? = null,
        milestone: String? = null,
        iids: Array<Long>? = null,
        orderBy: OrderBy? = OrderBy.UPDATED_AT,
        sort: Sort? = Sort.ASC,
        search: String? = null,
        page: Int,
        pageSize: Int = defaultPageSize,
        callback: (result: List<TargetHeader>?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.getIssues(projectId, scope, state, labels, milestone, iids, orderBy, sort, search, page, pageSize) }
    }
}