package gitfox.model.interactor

import gitfox.entity.*
import gitfox.entity.EventAction
import gitfox.entity.MergeRequestState
import gitfox.entity.app.CommitWithShortUser
import gitfox.entity.app.target.*
import gitfox.model.data.server.GitlabApi
import gitfox.model.data.state.ServerChanges
import gitfox.util.resolveMarkdownUrl
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow

class MergeRequestInteractor internal constructor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val defaultPageSize: Int
) {

    val mergeRequestChanges: Flow<Long> = serverChanges.mergeRequestChanges

    suspend fun getMyMergeRequests(
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
    ): List<TargetHeader> {
        val mrs = api.getMyMergeRequests(
            state, milestone, viewType, labels, createdBefore, createdAfter, scope,
            authorId, assigneeId, meReactionEmoji, orderBy, sort, page, pageSize
        )
        val projects = getDistinctProjects(mrs)
        return mrs.map { getTargetHeader(it, projects[it.projectId]!!) }
    }

    suspend fun getMergeRequests(
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
    ): List<TargetHeader> {
        val mrs = api.getMergeRequests(
            projectId, state, milestone, viewType, labels, createdBefore, createdAfter,
            scope, authorId, assigneeId, meReactionEmoji, orderBy, sort, page, pageSize
        )
        val projects = getDistinctProjects(mrs)
        return mrs.map { getTargetHeader(it, projects[it.projectId]!!) }
    }

    private suspend fun getDistinctProjects(mrs: List<MergeRequest>): Map<Long, Project> =
        mrs.distinctBy { it.projectId }.associate { mr ->
            mr.projectId to api.getProject(mr.projectId)
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

    suspend fun getMergeRequest(
        projectId: Long,
        mergeRequestId: Long
    ): MergeRequest = coroutineScope {
        val projectAsync = async { api.getProject(projectId) }
        val mr = api.getMergeRequest(projectId, mergeRequestId)

        val resolved = mr.description?.resolveMarkdownUrl(projectAsync.await())
        if (resolved != mr.description) mr.copy(description = resolved)
        else mr
    }

    suspend fun getMergeRequestNotes(
        projectId: Long,
        mergeRequestId: Long,
        sort: Sort? = Sort.ASC,
        orderBy: OrderBy? = OrderBy.UPDATED_AT,
        page: Int,
        pageSize: Int = defaultPageSize
    ): List<Note> = coroutineScope {
        val projectAsync = async { api.getProject(projectId) }
        val notes =
            api.getMergeRequestNotes(projectId, mergeRequestId, sort, orderBy, page, pageSize)
        notes.map { resolveMarkDownUrl(it, projectAsync.await()) }
    }

    suspend fun getAllMergeRequestNotes(
        projectId: Long,
        mergeRequestId: Long,
        sort: Sort? = Sort.ASC,
        orderBy: OrderBy = OrderBy.UPDATED_AT
    ): List<Note> = coroutineScope {
        val projectAsync = async { api.getProject(projectId) }
        val notes = buildList {
            var i = 1
            do {
                val page = api.getMergeRequestNotes(
                    projectId, mergeRequestId, sort,
                    orderBy, i, GitlabApi.MAX_PAGE_SIZE
                )
                addAll(page)
                i++
            } while (page.isNotEmpty())
        }
        notes.map { resolveMarkDownUrl(it, projectAsync.await()) }
    }

    private fun resolveMarkDownUrl(it: Note, project: Project): Note {
        val resolved = it.body.resolveMarkdownUrl(project)
        return if (resolved != it.body) it.copy(body = resolved) else it
    }

    suspend fun createMergeRequestNote(projectId: Long, issueId: Long, body: String): Note =
        api.createMergeRequestNote(projectId, issueId, body)

    suspend fun getMergeRequestCommits(
        projectId: Long,
        mergeRequestId: Long,
        page: Int,
        pageSize: Int = defaultPageSize
    ): List<CommitWithShortUser> = coroutineScope {
        val commitsAsync = async {
            api.getMergeRequestCommits(projectId, mergeRequestId, page, pageSize)
        }
        val participants: List<ShortUser> = buildList {
            var i = 1
            do {
                val p = api.getMergeRequestParticipants(
                    projectId, mergeRequestId, i, GitlabApi.MAX_PAGE_SIZE
                )
                addAll(p)
                i++
            } while (p.isNotEmpty())
        }
        commitsAsync.await().map { commit ->
            CommitWithShortUser(
                commit,
                participants.find { it.name == commit.authorName || it.username == commit.authorName }
            )
        }
    }

    suspend fun getMergeRequestDiffDataList(projectId: Long, mergeRequestId: Long): List<DiffData> {
        val mr = api.getMergeRequestDiffDataList(projectId, mergeRequestId)
        return mr.diffDataList ?: arrayListOf()
    }
}
