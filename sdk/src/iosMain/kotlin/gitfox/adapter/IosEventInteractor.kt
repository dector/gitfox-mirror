package gitfox.adapter

import gitfox.entity.*
import gitfox.entity.app.target.TargetHeader
import gitfox.model.interactor.EventInteractor
import kotlinx.coroutines.CoroutineScope

class IosEventInteractor internal constructor(
    private val interactor: EventInteractor,
    private val defaultPageSize: Int
) : CoroutineScope by CoroutineScope(MainLoopDispatcher) {

    fun getEvents(
        action: EventAction? = null,
        targetType: EventTarget? = null,
        beforeDay: Date? = null,
        afterDay: Date? = null,
        sort: Sort? = Sort.DESC,
        orderBy: OrderBy = OrderBy.UPDATED_AT,
        page: Int,
        pageSize: Int = defaultPageSize,
        callback: (result: List<TargetHeader>?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.getEvents(action, targetType, beforeDay, afterDay, sort, orderBy, page, pageSize) }
    }

    fun getProjectEvents(
        projectId: Long,
        action: EventAction? = null,
        targetType: EventTarget? = null,
        beforeDay: Date? = null,
        afterDay: Date? = null,
        sort: Sort? = Sort.DESC,
        orderBy: OrderBy = OrderBy.UPDATED_AT,
        page: Int,
        pageSize: Int = defaultPageSize,
        callback: (result: List<TargetHeader>?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.getProjectEvents(projectId, action, targetType, beforeDay, afterDay, sort, orderBy, page, pageSize) }
    }

}