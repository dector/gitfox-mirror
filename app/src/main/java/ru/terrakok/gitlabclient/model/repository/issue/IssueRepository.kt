package ru.terrakok.gitlabclient.model.repository.issue

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.Sort
import ru.terrakok.gitlabclient.entity.app.target.*
import ru.terrakok.gitlabclient.entity.event.EventAction
import ru.terrakok.gitlabclient.entity.issue.Issue
import ru.terrakok.gitlabclient.entity.issue.IssueScope
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultPageSize
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 14.06.17.
 */
class IssueRepository @Inject constructor(
        private val api: GitlabApi,
        private val schedulers: SchedulersProvider,
        @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) {
    private val defaultPageSize = defaultPageSizeWrapper.value

    fun getMyIssues(
            scope: IssueScope? = null,
            state: IssueState? = null,
            labels: String? = null,
            milestone: String? = null,
            iids: Array<Long>? = null,
            orderBy: OrderBy? = null,
            sort: Sort? = null,
            search: String? = null,
            page: Int,
            pageSize: Int = defaultPageSize
    ) = api
            .getMyIssues(scope, state, labels, milestone, iids, orderBy, sort, search, page, pageSize)
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
                .flatMapSingle { issue -> api.getProject(issue.projectId) }
                .toMap { it.id }
    }

    private fun getTargetHeader(issue: Issue, project: Project): TargetHeader {
        val badges = mutableListOf<TargetBadge>()
        badges.add(TargetBadge.Status(when(issue.state) {
            IssueState.OPENED -> TargetBadgeStatus.OPENED
            IssueState.CLOSED -> TargetBadgeStatus.CLOSED
        }))
        badges.add(TargetBadge.Text(project.name, AppTarget.PROJECT, project.id))
        badges.add(TargetBadge.Text(issue.author.username, AppTarget.USER, issue.author.id))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.COMMENTS, issue.userNotesCount))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.UP_VOTES, issue.upvotes))
        badges.add(TargetBadge.Icon(TargetBadgeIcon.DOWN_VOTES, issue.downvotes))
        issue.labels.forEach { label -> badges.add(TargetBadge.Text(label)) }

        return TargetHeader(
                issue.author,
                TargetHeaderIcon.NONE,
                TargetHeaderTitle.Event(
                        issue.author.name,
                        EventAction.CREATED,
                        "${AppTarget.ISSUE} #${issue.iid}",
                        project.name
                ),
                issue.title,
                issue.createdAt,
                AppTarget.ISSUE,
                issue.id,
                TargetInternal(issue.projectId, issue.iid),
                badges
        )
    }

    fun getIssue(
            projectId: Long,
            issueId: Long
    ) = api
            .getIssue(projectId, issueId)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getIssueNotes(
            projectId: Long,
            issueId: Long,
            orderBy: OrderBy? = null,
            sort: Sort? = Sort.ASC
    ) = api
            .getIssueNotes(projectId, issueId, orderBy, sort)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}