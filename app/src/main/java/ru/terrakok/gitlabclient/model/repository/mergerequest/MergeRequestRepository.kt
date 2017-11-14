package ru.terrakok.gitlabclient.model.repository.mergerequest

import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.Commit
import ru.terrakok.gitlabclient.entity.Note
import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.Sort
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestScope
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestViewType
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultPageSize
import java.util.*
import javax.inject.Inject

class MergeRequestRepository @Inject constructor(
        private val api: GitlabApi,
        private val schedulers: SchedulersProvider,
        @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) {
    private val defaultPageSize = defaultPageSizeWrapper.value

    fun getMergeRequests(
            state: MergeRequestState? = null,
            milestone: String? = null,
            viewType: MergeRequestViewType? = null,
            labels: String? = null,
            createdBefore: Date? = null,
            createdAfter: Date? = null,
            scope: MergeRequestScope? = null,
            authorId: Int? = null,
            assigneeId: Int? = null,
            meReactionEmoji: String? = null,
            orderBy: OrderBy? = null,
            sort: Sort? = null,
            page: Int,
            pageSize: Int = defaultPageSize
    ) = api
            .getMergeRequests(
                    state,
                    milestone,
                    viewType,
                    labels,
                    createdBefore,
                    createdAfter,
                    scope,
                    authorId,
                    assigneeId,
                    meReactionEmoji,
                    orderBy,
                    sort,
                    page,
                    pageSize
            )
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getProjectMergeRequests(
            projectId: Int,
            state: MergeRequestState? = null,
            milestone: String? = null,
            viewType: MergeRequestViewType? = null,
            labels: String? = null,
            createdBefore: Date? = null,
            createdAfter: Date? = null,
            scope: MergeRequestScope? = null,
            authorId: Int? = null,
            assigneeId: Int? = null,
            meReactionEmoji: String? = null,
            orderBy: OrderBy? = null,
            sort: Sort? = null,
            page: Int,
            pageSize: Int = defaultPageSize
    ) = api
            .getProjectMergeRequests(
                    projectId,
                    state,
                    milestone,
                    viewType,
                    labels,
                    createdBefore,
                    createdAfter,
                    scope,
                    authorId,
                    assigneeId,
                    meReactionEmoji,
                    orderBy,
                    sort,
                    page,
                    pageSize
            )
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getMergeRequest(
            projectId: Int,
            mergeRequestId: Int
    ) = api
            .getMergeRequest(projectId, mergeRequestId)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getMergeRequestCommits(
            projectId: Int,
            mergeRequestId: Int
    ): Single<List<Commit>> = api
            .getMergeRequestCommits(projectId, mergeRequestId)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getMergeRequestChanges(
            projectId: Int,
            mergeRequestId: Int
    ): Single<MergeRequest> = api
            .getMergeRequestChanges(projectId, mergeRequestId)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getMergeRequestNotes(
            projectId: Int,
            mergeRequestId: Int
    ): Single<List<Note>> = api
            .getMergeRequestNotes(projectId, mergeRequestId)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}