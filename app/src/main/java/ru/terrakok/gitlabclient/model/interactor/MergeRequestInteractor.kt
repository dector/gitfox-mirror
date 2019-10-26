package ru.terrakok.gitlabclient.model.interactor

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.threeten.bp.ZonedDateTime
import ru.terrakok.gitlabclient.di.DefaultPageSize
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.*
import ru.terrakok.gitlabclient.entity.app.CommitWithShortUser
import ru.terrakok.gitlabclient.entity.app.target.*
import ru.terrakok.gitlabclient.entity.event.EventAction
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestScope
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestViewType
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.MarkDownUrlResolver
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class MergeRequestInteractor @Inject constructor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val schedulers: SchedulersProvider,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>,
    private val markDownUrlResolver: MarkDownUrlResolver
) {
    private val defaultPageSize = defaultPageSizeWrapper.value
    private val mrRequests = ConcurrentHashMap<Pair<Long, Long>, Single<MergeRequest>>()

    val mergeRequestChanges = serverChanges.mergeRequestChanges

    fun getMyMergeRequests(
        state: MergeRequestState? = null,
        milestone: String? = null,
        viewType: MergeRequestViewType? = null,
        labels: String? = null,
        createdBefore: ZonedDateTime? = null,
        createdAfter: ZonedDateTime? = null,
        scope: MergeRequestScope? = null,
        authorId: Int? = null,
        assigneeId: Int? = null,
        meReactionEmoji: String? = null,
        orderBy: OrderBy? = OrderBy.UPDATED_AT,
        sort: Sort? = null,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = api
        .getMyMergeRequests(
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

    fun getMergeRequests(
        projectId: Long,
        state: MergeRequestState? = null,
        milestone: String? = null,
        viewType: MergeRequestViewType? = null,
        labels: String? = null,
        createdBefore: ZonedDateTime? = null,
        createdAfter: ZonedDateTime? = null,
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
            projectId, state, milestone, viewType, labels, createdBefore, createdAfter,
            scope, authorId, assigneeId, meReactionEmoji, orderBy, sort, page, pageSize
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
        badges.add(
            TargetBadge.Status(
                when (mr.state) {
                    MergeRequestState.OPENED -> TargetBadgeStatus.OPENED
                    MergeRequestState.CLOSED -> TargetBadgeStatus.CLOSED
                    MergeRequestState.MERGED -> TargetBadgeStatus.MERGED
                }
            )
        )
        badges.add(TargetBadge.Text(project.name, AppTarget.PROJECT, project.id))
        badges.add(TargetBadge.Text(mr.author.username, AppTarget.USER, mr.author.id))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.COMMENTS, mr.userNotesCount))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.UP_VOTES, mr.upvotes))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.DOWN_VOTES, mr.downvotes))
        mr.labels.forEach { label -> badges.add(TargetBadge.Text(label)) }

        return TargetHeader.Public(
            mr.author,
            TargetHeaderIcon.NONE,
            TargetHeaderTitle.Event(
                mr.author.name,
                EventAction.CREATED,
                "${AppTarget.MERGE_REQUEST} !${mr.iid}",
                project.name
            ),
            mr.title ?: "",
            mr.createdAt,
            AppTarget.MERGE_REQUEST,
            mr.id,
            TargetInternal(mr.projectId, mr.iid),
            badges,
            TargetAction.Undefined
        )
    }

    fun getMergeRequest(
        projectId: Long,
        mergeRequestId: Long
    ) = Single
        .defer {
            val key = Pair(projectId, mergeRequestId)
            mrRequests.getOrPut(key) {
                Single
                    .zip(
                        api.getProject(projectId),
                        api.getMergeRequest(projectId, mergeRequestId),
                        BiFunction<Project, MergeRequest, MergeRequest> { project, mr ->
                            val resolved = markDownUrlResolver.resolve(mr.description, project)
                            if (resolved != mr.description) mr.copy(description = resolved)
                            else mr
                        }
                    )
                    .toObservable()
                    .share()
                    .firstOrError()
            }
        }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getMergeRequestNotes(
        projectId: Long,
        mergeRequestId: Long,
        sort: Sort? = Sort.ASC,
        orderBy: OrderBy? = OrderBy.UPDATED_AT,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = Single
        .zip(
            api.getProject(projectId),
            api.getMergeRequestNotes(projectId, mergeRequestId, sort, orderBy, page, pageSize),
            BiFunction<Project, List<Note>, List<Note>> { project, notes ->
                notes.map { resolveMarkDownUrl(it, project) }
            }
        )
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getAllMergeRequestNotes(
        projectId: Long,
        mergeRequestId: Long,
        sort: Sort? = Sort.ASC,
        orderBy: OrderBy = OrderBy.UPDATED_AT
    ) = Single
        .zip(
            api.getProject(projectId),
            getAllMergeRequestNotePages(projectId, mergeRequestId, sort, orderBy),
            BiFunction<Project, List<Note>, List<Note>> { project, notes ->
                notes.map { resolveMarkDownUrl(it, project) }
            }
        )
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    private fun getAllMergeRequestNotePages(projectId: Long, mergeRequestId: Long, sort: Sort?, orderBy: OrderBy?) =
        Observable.range(1, Int.MAX_VALUE)
            .concatMap { page ->
                api.getMergeRequestNotes(projectId, mergeRequestId, sort, orderBy, page, GitlabApi.MAX_PAGE_SIZE)
                    .toObservable()
            }
            .takeWhile { notes -> notes.isNotEmpty() }
            .flatMapIterable { it }
            .toList()

    private fun resolveMarkDownUrl(it: Note, project: Project): Note {
        val resolved = markDownUrlResolver.resolve(it.body, project)
        return if (resolved != it.body) it.copy(body = resolved) else it
    }

    fun createMergeRequestNote(projectId: Long, issueId: Long, body: String) =
        api.createMergeRequestNote(projectId, issueId, body)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getMergeRequestCommits(
        projectId: Long,
        mergeRequestId: Long,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = Single
        .zip(
            getAllMergeRequestParticipants(projectId, mergeRequestId),
            api.getMergeRequestCommits(projectId, mergeRequestId, page, pageSize),
            BiFunction<List<ShortUser>, List<Commit>, List<CommitWithShortUser>> { participants, commits ->
                commits.map { commit ->
                    CommitWithShortUser(
                        commit,
                        participants.find { it.name == commit.authorName || it.username == commit.authorName }
                    )
                }
            }
        )
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    private fun getAllMergeRequestParticipants(projectId: Long, mergeRequestId: Long) =
        Observable.range(1, Int.MAX_VALUE)
            .concatMap { page ->
                api.getMergeRequestParticipants(projectId, mergeRequestId, page, GitlabApi.MAX_PAGE_SIZE)
                    .toObservable()
            }
            .takeWhile { participants -> participants.isNotEmpty() }
            .flatMapIterable { it }
            .toList()

    fun getMergeRequestDiffDataList(projectId: Long, mergeRequestId: Long) =
        api.getMergeRequestDiffDataList(projectId, mergeRequestId)
            .map { it.diffDataList ?: arrayListOf() }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}