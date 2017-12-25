package ru.terrakok.gitlabclient.model.repository.mergerequest

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.Sort
import ru.terrakok.gitlabclient.entity.app.target.*
import ru.terrakok.gitlabclient.entity.event.EventAction
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
                    state, milestone, viewType, labels, createdBefore, createdAfter, scope,
                    authorId, assigneeId, meReactionEmoji, orderBy, sort, page, pageSize
            )
            .flatMap { mrs ->
                Single.zip(
                        Single.just(mrs),
                        getDistinctProjects(mrs),
                        BiFunction<List<MergeRequest>, Map<Long, Project>, List<TargetHeader>> { sourceMrs, projects ->
                            sourceMrs.map { getTargetHeader(it, projects[it.projectId]!!) }
                        }
                )
            }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    private fun getDistinctProjects(mrs: List<MergeRequest>): Single<Map<Long, Project>> {
        return Observable.fromIterable(mrs)
                .distinct { it.projectId }
                .flatMapSingle { mr -> api.getProject(mr.projectId) }
                .toMap { it.id }
    }

    private fun getTargetHeader(mr: MergeRequest, project: Project): TargetHeader {
        val badges = mutableListOf<TargetBadge>()
        badges.add(TargetBadge.Text(project.name, AppTarget.PROJECT, project.id))
        badges.add(TargetBadge.Text(mr.author.username, AppTarget.USER, mr.author.id))
        badges.add(TargetBadge.Comments(mr.userNotesCount))

        return TargetHeader(
                mr.author,
                TargetHeaderIcon.NONE,
                TargetHeaderTitle.Event(
                        mr.author.name,
                        EventAction.CREATED,
                        "${AppTarget.MERGE_REQUEST} !${mr.iid}",
                        project.nameWithNamespace
                ),
                mr.title,
                mr.createdAt,
                AppTarget.ISSUE,
                mr.id,
                badges
        )
    }
}