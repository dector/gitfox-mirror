package ru.terrakok.gitlabclient.model.interactor

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import kotlinx.coroutines.rx2.rxCompletable
import kotlinx.coroutines.rx2.rxSingle
import ru.terrakok.gitlabclient.di.DefaultPageSize
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.*
import ru.terrakok.gitlabclient.entity.app.target.*
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.MarkDownUrlResolver
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 14.06.17.
 */
class IssueInteractor @Inject constructor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val schedulers: SchedulersProvider,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>,
    private val markDownUrlResolver: MarkDownUrlResolver
) {
    private val defaultPageSize = defaultPageSizeWrapper.value
    private val issueRequests = ConcurrentHashMap<Pair<Long, Long>, Single<Issue>>()

    val issueChanges = serverChanges.issueChanges

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
    ) = rxSingle {
        api.getMyIssues(
            scope,
            state,
            labels,
            milestone,
            iids,
            orderBy,
            sort,
            search,
            page,
            pageSize
        )
    }
        .flatMap { issues ->
            Single.zip(
                Single.just(issues),
                getDistinctProjects(issues),
                BiFunction<List<Issue>, Map<Long, Project>, List<TargetHeader>> { sourceIssues, projects ->
                    sourceIssues.map { getTargetHeader(it, projects[it.projectId]!!) }
                }
            )
        }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

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
    ) = rxSingle {
        api.getIssues(
            projectId,
            scope,
            state,
            labels,
            milestone,
            iids,
            orderBy,
            sort,
            search,
            page,
            pageSize
        )
    }
        .flatMap { issues ->
            Single.zip(
                Single.just(issues),
                getDistinctProjects(issues),
                BiFunction<List<Issue>, Map<Long, Project>, List<TargetHeader>> { sourceIssues, projects ->
                    sourceIssues.map { getTargetHeader(it, projects[it.projectId]!!) }
                }
            )
        }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    private fun getDistinctProjects(events: List<Issue>): Single<Map<Long, Project>> {
        return Observable.fromIterable(events)
            .distinct { it.projectId }
            .flatMapSingle { issue -> rxSingle { api.getProject(issue.projectId) } }
            .toMap { it.id }
    }

    private fun getTargetHeader(issue: Issue, project: Project): TargetHeader {
        val badges = mutableListOf<TargetBadge>()
        badges.add(
            TargetBadge.Status(
                when (issue.state) {
                    IssueState.OPENED -> TargetBadgeStatus.OPENED
                    IssueState.CLOSED -> TargetBadgeStatus.CLOSED
                }
            )
        )
        badges.add(TargetBadge.Text(project.name, AppTarget.PROJECT, project.id))
        badges.add(TargetBadge.Text(issue.author.username, AppTarget.USER, issue.author.id))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.COMMENTS, issue.userNotesCount))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.UP_VOTES, issue.upvotes))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.DOWN_VOTES, issue.downvotes))
        badges.add(
            TargetBadge.Icon(
                TargetBadgeIcon.RELATED_MERGE_REQUESTS,
                issue.relatedMergeRequestCount
            )
        )
        issue.labels.forEach { label -> badges.add(TargetBadge.Text(label)) }

        return TargetHeader.Public(
            issue.author,
            TargetHeaderIcon.NONE,
            TargetHeaderTitle.Event(
                issue.author.name,
                EventAction.CREATED,
                "${AppTarget.ISSUE} #${issue.iid}",
                project.name
            ),
            issue.title ?: "",
            issue.createdAt,
            AppTarget.ISSUE,
            issue.id,
            TargetInternal(issue.projectId, issue.iid),
            badges,
            TargetAction.Undefined
        )
    }

    fun getIssue(
        projectId: Long,
        issueId: Long
    ) = Single
        .defer {
            val key = Pair(projectId, issueId)
            issueRequests.getOrPut(key) {
                Single
                    .zip(
                        rxSingle { api.getProject(projectId) },
                        rxSingle { api.getIssue(projectId, issueId) },
                        BiFunction<Project, Issue, Issue> { project, issue ->
                            val resolved = markDownUrlResolver.resolve(issue.description, project)
                            if (resolved != issue.description) issue.copy(description = resolved)
                            else issue
                        }
                    )
                    .toObservable()
                    .share()
                    .firstOrError()
            }
        }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getIssueNotes(
        projectId: Long,
        issueId: Long,
        sort: Sort?,
        orderBy: OrderBy?,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = Single
        .zip(
            rxSingle { api.getProject(projectId) },
            rxSingle { api.getIssueNotes(projectId, issueId, sort, orderBy, page, pageSize) },
            BiFunction<Project, List<Note>, List<Note>> { project, notes ->
                notes.map { resolveMarkDownUrl(it, project) }
            }
        )
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getAllIssueNotes(
        projectId: Long,
        issueId: Long,
        sort: Sort? = Sort.ASC,
        orderBy: OrderBy? = OrderBy.UPDATED_AT
    ) = Single
        .zip(
            rxSingle { api.getProject(projectId) },
            getAllIssueNotePages(projectId, issueId, sort, orderBy),
            BiFunction<Project, List<Note>, List<Note>> { project, notes ->
                notes.map { resolveMarkDownUrl(it, project) }
            }
        )
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    private fun getAllIssueNotePages(
        projectId: Long,
        issueId: Long,
        sort: Sort?,
        orderBy: OrderBy?
    ) =
        Observable.range(1, Int.MAX_VALUE)
            .concatMap { page ->
                rxSingle {
                    api.getIssueNotes(
                        projectId,
                        issueId,
                        sort,
                        orderBy,
                        page,
                        GitlabApi.MAX_PAGE_SIZE
                    )
                }
                    .toObservable()
            }
            .takeWhile { notes -> notes.isNotEmpty() }
            .flatMapIterable { it }
            .toList()

    private fun resolveMarkDownUrl(it: Note, project: Project): Note {
        val resolved = markDownUrlResolver.resolve(it.body, project)
        return if (resolved != it.body) it.copy(body = resolved) else it
    }

    fun createIssueNote(projectId: Long, issueId: Long, body: String) =
        rxSingle { api.createIssueNote(projectId, issueId, body) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun closeIssue(projectId: Long, issueId: Long) =
        rxCompletable { api.editIssue(projectId, issueId, IssueStateEvent.CLOSE) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}
