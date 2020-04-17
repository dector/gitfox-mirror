package gitfox.adapter

import gitfox.entity.*
import gitfox.model.interactor.EventInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

class JsEventInteractor internal constructor(
    private val interactor: EventInteractor,
    private val defaultPageSize: Int
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    @JsName("getEvents")
    fun getEvents(
        action: EventAction? = null,
        targetType: EventTarget? = null,
        beforeDay: Date? = null,
        afterDay: Date? = null,
        sort: Sort? = Sort.DESC,
        orderBy: OrderBy = OrderBy.UPDATED_AT,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = promise {
        interactor.getEvents(action, targetType, beforeDay, afterDay, sort, orderBy, page, pageSize)
    }

    @JsName("getProjectEvents")
    fun getProjectEvents(
        projectId: Long,
        action: EventAction? = null,
        targetType: EventTarget? = null,
        beforeDay: Date? = null,
        afterDay: Date? = null,
        sort: Sort? = Sort.DESC,
        orderBy: OrderBy = OrderBy.UPDATED_AT,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = promise {
        interactor.getProjectEvents(projectId, action, targetType, beforeDay, afterDay, sort, orderBy, page, pageSize)
    }

}