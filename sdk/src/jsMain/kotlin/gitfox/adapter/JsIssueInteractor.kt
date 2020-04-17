package gitfox.adapter

import gitfox.entity.IssueScope
import gitfox.entity.IssueState
import gitfox.entity.OrderBy
import gitfox.entity.Sort
import gitfox.model.interactor.IssueInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

class JsIssueInteractor internal constructor(
    private val interactor: IssueInteractor,
    private val defaultPageSize: Int
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    @JsName("getMyIssues")
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
        pageSize: Int = defaultPageSize
    ) = promise {
        interactor.getMyIssues(scope, state, labels, milestone, iids, orderBy, sort, search, page, pageSize)
    }

    @JsName("getIssues")
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
        pageSize: Int = defaultPageSize
    ) = promise {
        interactor.getIssues(projectId, scope, state, labels, milestone, iids, orderBy, sort, search, page, pageSize)
    }
}