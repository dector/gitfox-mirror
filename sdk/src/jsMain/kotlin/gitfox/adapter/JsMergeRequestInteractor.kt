package gitfox.adapter

import gitfox.entity.*
import gitfox.model.interactor.MergeRequestInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

class JsMergeRequestInteractor internal constructor(
    private val interactor: MergeRequestInteractor,
    private val defaultPageSize: Int
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {
    val mergeRequestChanges = interactor.mergeRequestChanges.wrap()

    fun getMyMergeRequests(
        state: MergeRequestState? = null,
        milestone: String? = null,
        viewType: MergeRequestViewType? = null,
        labels: String? = null,
        createdBefore: Time? = null,
        createdAfter: Time? = null,
        scope: MergeRequestScope? = null,
        authorId: Int? = null,
        assigneeId: Int? = null,
        meReactionEmoji: String? = null,
        orderBy: OrderBy? = OrderBy.UPDATED_AT,
        sort: Sort? = null,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = promise {
        interactor.getMyMergeRequests(state, milestone, viewType, labels, createdBefore, createdAfter, scope, authorId, assigneeId, meReactionEmoji, orderBy, sort, page, pageSize)
    }

    fun getMergeRequests(
        projectId: Long,
        state: MergeRequestState? = null,
        milestone: String? = null,
        viewType: MergeRequestViewType? = null,
        labels: String? = null,
        createdBefore: Time? = null,
        createdAfter: Time? = null,
        scope: MergeRequestScope? = null,
        authorId: Int? = null,
        assigneeId: Int? = null,
        meReactionEmoji: String? = null,
        orderBy: OrderBy? = null,
        sort: Sort? = null,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = promise {
        interactor.getMergeRequests(projectId, state, milestone, viewType, labels, createdBefore, createdAfter, scope, authorId, assigneeId, meReactionEmoji, orderBy, sort, page, pageSize)
    }

    fun getMergeRequest(
        projectId: Long,
        mergeRequestId: Long
    ) = promise {
        interactor.getMergeRequest(projectId, mergeRequestId)
    }

    fun getMergeRequestNotes(
        projectId: Long,
        mergeRequestId: Long,
        sort: Sort? = Sort.ASC,
        orderBy: OrderBy? = OrderBy.UPDATED_AT,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = promise {
        interactor.getMergeRequestNotes(projectId, mergeRequestId, sort, orderBy, page, pageSize)
    }

    fun getAllMergeRequestNotes(
        projectId: Long,
        mergeRequestId: Long,
        sort: Sort? = Sort.ASC,
        orderBy: OrderBy = OrderBy.UPDATED_AT
    ) = promise {
        interactor.getAllMergeRequestNotes(projectId, mergeRequestId, sort, orderBy)
    }

    fun createMergeRequestNote(
        projectId: Long,
        issueId: Long,
        body: String
    ) = promise {
        interactor.createMergeRequestNote(projectId, issueId, body)
    }

    fun getMergeRequestCommits(
        projectId: Long,
        mergeRequestId: Long,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = promise {
        interactor.getMergeRequestCommits(projectId, mergeRequestId, page, pageSize)
    }

    fun getMergeRequestDiffDataList(
        projectId: Long,
        mergeRequestId: Long
    ) = promise {
        interactor.getMergeRequestDiffDataList(projectId, mergeRequestId)
    }
}