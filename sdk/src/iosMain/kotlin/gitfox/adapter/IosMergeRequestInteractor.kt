package gitfox.adapter

import gitfox.entity.*
import gitfox.entity.app.CommitWithShortUser
import gitfox.entity.app.target.TargetHeader
import gitfox.model.interactor.MergeRequestInteractor
import kotlinx.coroutines.CoroutineScope

class IosMergeRequestInteractor internal constructor(
    private val interactor: MergeRequestInteractor,
    private val defaultPageSize: Int
) : CoroutineScope by CoroutineScope(MainLoopDispatcher) {
//    val mergeRequestChanges: Flow<Long>

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
        pageSize: Int = defaultPageSize,
        callback: (result: List<TargetHeader>?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getMyMergeRequests(state, milestone, viewType, labels, createdBefore, createdAfter, scope, authorId, assigneeId, meReactionEmoji, orderBy, sort, page, pageSize) }
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
        pageSize: Int = defaultPageSize,
        callback: (result: List<TargetHeader>?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getMergeRequests(projectId, state, milestone, viewType, labels, createdBefore, createdAfter, scope, authorId, assigneeId, meReactionEmoji, orderBy, sort, page, pageSize) }
    }

    fun getMergeRequest(
        projectId: Long,
        mergeRequestId: Long,
        callback: (result: MergeRequest?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getMergeRequest(projectId, mergeRequestId) }
    }

    fun getMergeRequestNotes(
        projectId: Long,
        mergeRequestId: Long,
        sort: Sort? = Sort.ASC,
        orderBy: OrderBy? = OrderBy.UPDATED_AT,
        page: Int,
        pageSize: Int = defaultPageSize,
        callback: (result: List<Note>?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getMergeRequestNotes(projectId, mergeRequestId, sort, orderBy, page, pageSize) }
    }

    fun getAllMergeRequestNotes(
        projectId: Long,
        mergeRequestId: Long,
        sort: Sort? = Sort.ASC,
        orderBy: OrderBy = OrderBy.UPDATED_AT,
        callback: (result: List<Note>?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getAllMergeRequestNotes(projectId, mergeRequestId, sort, orderBy) }
    }

    fun createMergeRequestNote(
        projectId: Long,
        issueId: Long,
        body: String,
        callback: (result: Note?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.createMergeRequestNote(projectId, issueId, body) }
    }

    fun getMergeRequestCommits(
        projectId: Long,
        mergeRequestId: Long,
        page: Int,
        pageSize: Int = defaultPageSize,
        callback: (result: List<CommitWithShortUser>?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getMergeRequestCommits(projectId, mergeRequestId, page, pageSize) }
    }

    fun getMergeRequestDiffDataList(
        projectId: Long,
        mergeRequestId: Long,
        callback: (result: List<DiffData>?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getMergeRequestDiffDataList(projectId, mergeRequestId) }
    }
}