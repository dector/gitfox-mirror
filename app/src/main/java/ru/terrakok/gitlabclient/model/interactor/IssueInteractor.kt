package ru.terrakok.gitlabclient.model.interactor

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import ru.terrakok.gitlabclient.di.DefaultPageSize
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.*
import ru.terrakok.gitlabclient.entity.app.target.*
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.MarkDownUrlResolver
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 14.06.17.
 */
class IssueInteractor @Inject constructor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>,
    private val markDownUrlResolver: MarkDownUrlResolver
) {
    private val defaultPageSize = defaultPageSizeWrapper.value

    val issueChanges: Flow<Long> = serverChanges.issueChanges

    suspend fun getMyIssues(
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
    ): List<TargetHeader> {
        val issues = api.getMyIssues(
            scope, state, labels,
            milestone, iids, orderBy,
            sort, search, page, pageSize
        )
        val projects = getDistinctProjects(issues)
        return issues.map { getTargetHeader(it, projects[it.projectId]!!) }
    }

    suspend fun getIssues(
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
    ): List<TargetHeader> {
        val issues = api.getIssues(
            projectId, scope, state, labels,
            milestone, iids, orderBy,
            sort, search, page, pageSize
        )
        val projects = getDistinctProjects(issues)
        return issues.map { getTargetHeader(it, projects[it.projectId]!!) }
    }

    private suspend fun getDistinctProjects(issues: List<Issue>): Map<Long, Project> =
        issues.distinctBy { it.projectId }.associate { issue ->
            issue.projectId to api.getProject(issue.projectId)
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

    suspend fun getIssue(
        projectId: Long,
        issueId: Long
    ): Issue = coroutineScope {
        val projectAsync = async { api.getProject(projectId) }
        val issue = api.getIssue(projectId, issueId)

        val resolved = markDownUrlResolver.resolve(issue.description, projectAsync.await())
        if (resolved != issue.description) issue.copy(description = resolved)
        else issue
    }

    suspend fun getIssueNotes(
        projectId: Long,
        issueId: Long,
        sort: Sort?,
        orderBy: OrderBy?,
        page: Int,
        pageSize: Int = defaultPageSize
    ): List<Note> = coroutineScope {
        val projectAsync = async { api.getProject(projectId) }
        val notes = api.getIssueNotes(projectId, issueId, sort, orderBy, page, pageSize)
        notes.map { resolveMarkDownUrl(it, projectAsync.await()) }
    }

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun getAllIssueNotes(
        projectId: Long,
        issueId: Long,
        sort: Sort? = Sort.ASC,
        orderBy: OrderBy? = OrderBy.UPDATED_AT
    ): List<Note> = coroutineScope {
        val projectAsync = async { api.getProject(projectId) }
        val notes = buildList {
            var i = 1
            do {
                val page = api.getIssueNotes(
                    projectId, issueId, sort,
                    orderBy, i, GitlabApi.MAX_PAGE_SIZE
                )
                addAll(page)
                i++
            } while (page.isNotEmpty())
        }
        notes.map { resolveMarkDownUrl(it, projectAsync.await()) }
    }

    private fun resolveMarkDownUrl(it: Note, project: Project): Note {
        val resolved = markDownUrlResolver.resolve(it.body, project)
        return if (resolved != it.body) it.copy(body = resolved) else it
    }

    suspend fun createIssueNote(projectId: Long, issueId: Long, body: String): Note =
        api.createIssueNote(projectId, issueId, body)

    suspend fun closeIssue(projectId: Long, issueId: Long) {
        api.editIssue(projectId, issueId, IssueStateEvent.CLOSE)
    }
}
