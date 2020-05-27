package gitfox.model.data.server

import gitfox.entity.*
import gitfox.model.data.server.GitlabApi.Companion.API_PATH
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Parameters
import io.ktor.http.encodeURLParameter
import io.ktor.http.takeFrom

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.03.17
 */
internal class GitlabApiImpl(
    override val endpoint: String,
    private val httpClient: HttpClient
) : GitlabApi {
    private val apiUrl = "$endpoint$API_PATH"

    override suspend fun getProjects(
        archived: Boolean?,
        visibility: Visibility?,
        orderBy: OrderBy?,
        sort: Sort?,
        search: String?,
        simple: Boolean?,
        owned: Boolean?,
        membership: Boolean?,
        starred: Boolean?,
        page: Int,
        pageSize: Int
    ): List<Project> = httpClient.get {
        url {
            takeFrom("$apiUrl/projects")
            parameter("archived", archived)
            parameter("visibility", visibility)
            parameter("order_by", orderBy)
            parameter("sort", sort)
            parameter("search", search)
            parameter("simple", simple)
            parameter("owned", owned)
            parameter("membership", membership)
            parameter("starred", starred)
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun getProject(
        id: Long,
        statistics: Boolean
    ): Project = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$id")
            parameter("statistics", statistics)
        }
    }

    override suspend fun getFile(
        id: Long,
        filePath: String,
        ref: String
    ): File = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$id/repository/files/${filePath.encodeURLParameter()}")
            parameter("ref", ref)
        }
    }

    override suspend fun getRepositoryTree(
        id: Long,
        path: String?,
        branchName: String?,
        recursive: Boolean?,
        page: Int,
        pageSize: Int
    ): List<RepositoryTreeNode> = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$id/repository/tree")
            parameter("path", path)
            parameter("ref", branchName)
            parameter("recursive", recursive)
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun getRepositoryCommit(
        projectId: Long,
        sha: String,
        stats: Boolean
    ): Commit = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$projectId/repository/commits/$sha")
            parameter("stats", stats)
        }
    }

    override suspend fun getCommitDiffData(
        projectId: Long,
        sha: String
    ): List<DiffData> = httpClient.get("$apiUrl/projects/$projectId/repository/commits/$sha/diff")

    override suspend fun getRepositoryCommits(
        projectId: Long,
        branchName: String?,
        since: String?,
        until: String?,
        path: String?,
        all: Boolean?,
        withStats: Boolean?
    ): List<Commit> = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$projectId/repository/commits/")
            parameter("ref_name", branchName)
            parameter("since", since)
            parameter("until", until)
            parameter("path", path)
            parameter("all", all)
            parameter("with_stats", withStats)
        }
    }

    override suspend fun getRepositoryBranches(
        projectId: Long
    ): List<Branch> = httpClient.get("$apiUrl/projects/$projectId/repository/branches/")

    override suspend fun getMyUser(): User = httpClient.get("$apiUrl/user")

    override suspend fun getMyIssues(
        scope: IssueScope?,
        state: IssueState?,
        labels: String?,
        milestone: String?,
        iids: Array<Long>?,
        orderBy: OrderBy?,
        sort: Sort?,
        search: String?,
        page: Int,
        pageSize: Int
    ): List<Issue> = httpClient.get {
        url {
            takeFrom("$apiUrl/issues")
            parameter("scope", scope)
            parameter("state", state)
            parameter("labels", labels)
            parameter("milestone", milestone)
            parameter("iids", iids)
            parameter("order_by", orderBy)
            parameter("sort", sort)
            parameter("search", search)
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun getIssues(
        projectId: Long,
        scope: IssueScope?,
        state: IssueState?,
        labels: String?,
        milestone: String?,
        iids: Array<Long>?,
        orderBy: OrderBy?,
        sort: Sort?,
        search: String?,
        page: Int,
        pageSize: Int
    ): List<Issue> = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$projectId/issues")
            parameter("scope", scope)
            parameter("state", state)
            parameter("labels", labels)
            parameter("milestone", milestone)
            parameter("iids", iids)
            parameter("order_by", orderBy)
            parameter("sort", sort)
            parameter("search", search)
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun getIssue(
        projectId: Long,
        issueId: Long
    ): Issue = httpClient.get("$apiUrl/projects/$projectId/issues/$issueId")

    override suspend fun editIssue(
        projectId: Long,
        issueId: Long,
        stateEvent: IssueStateEvent
    ) {
        httpClient.put<Unit>("$apiUrl/projects/$projectId/issues/$issueId") {
            body = FormDataContent(Parameters.build {
                append("state_event", stateEvent.toString())
            })
        }
    }

    override suspend fun getEvents(
        action: EventAction?,
        targetType: EventTarget?,
        beforeDay: Date?,
        afterDay: Date?,
        sort: Sort?,
        orderBy: OrderBy?,
        scope: EventScope?,
        page: Int,
        pageSize: Int
    ): List<Event> = httpClient.get {
        url {
            takeFrom("$apiUrl/events")
            parameter("action", action)
            parameter("target_type", targetType)
            parameter("before", beforeDay)
            parameter("after", afterDay)
            parameter("sort", sort)
            parameter("order_by", orderBy)
            parameter("scope", scope)
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun getProjectEvents(
        projectId: Long,
        action: EventAction?,
        targetType: EventTarget?,
        beforeDay: Date?,
        afterDay: Date?,
        sort: Sort?,
        orderBy: OrderBy?,
        page: Int,
        pageSize: Int
    ): List<Event> = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$projectId/events")
            parameter("action", action)
            parameter("target_type", targetType)
            parameter("before", beforeDay)
            parameter("after", afterDay)
            parameter("sort", sort)
            parameter("order_by", orderBy)
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun getMyMergeRequests(
        state: MergeRequestState?,
        milestone: String?,
        viewType: MergeRequestViewType?,
        labels: String?,
        createdBefore: Time?,
        createdAfter: Time?,
        scope: MergeRequestScope?,
        authorId: Int?,
        assigneeId: Int?,
        meReactionEmoji: String?,
        orderBy: OrderBy?,
        sort: Sort?,
        page: Int,
        pageSize: Int
    ): List<MergeRequest> = httpClient.get {
        url {
            takeFrom("$apiUrl/merge_requests")
            parameter("state", state)
            parameter("milestone", milestone)
            parameter("view", viewType)
            parameter("labels", labels)
            parameter("created_before", createdBefore)
            parameter("created_after", createdAfter)
            parameter("scope", scope)
            parameter("author_id", authorId)
            parameter("assignee_id", assigneeId)
            parameter("my_reaction_emoji", meReactionEmoji)
            parameter("order_by", orderBy)
            parameter("sort", sort)
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun getMergeRequests(
        projectId: Long,
        state: MergeRequestState?,
        milestone: String?,
        viewType: MergeRequestViewType?,
        labels: String?,
        createdBefore: Time?,
        createdAfter: Time?,
        scope: MergeRequestScope?,
        authorId: Int?,
        assigneeId: Int?,
        meReactionEmoji: String?,
        orderBy: OrderBy?,
        sort: Sort?,
        page: Int,
        pageSize: Int
    ): List<MergeRequest> = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$projectId/merge_requests")
            parameter("state", state)
            parameter("milestone", milestone)
            parameter("view", viewType)
            parameter("labels", labels)
            parameter("created_before", createdBefore)
            parameter("created_after", createdAfter)
            parameter("scope", scope)
            parameter("author_id", authorId)
            parameter("assignee_id", assigneeId)
            parameter("my_reaction_emoji", meReactionEmoji)
            parameter("order_by", orderBy)
            parameter("sort", sort)
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun getMergeRequest(
        projectId: Long,
        mergeRequestId: Long
    ): MergeRequest = httpClient.get("$apiUrl/projects/$projectId/merge_requests/$mergeRequestId")

    override suspend fun getUser(
        userId: Long
    ): User = httpClient.get("$apiUrl/users/$userId")

    override suspend fun getTodos(
        action: TodoAction?,
        authorId: Long?,
        projectId: Long?,
        state: TodoState?,
        targetType: TargetType?,
        page: Int,
        pageSize: Int
    ): List<Todo> = httpClient.get {
        url {
            takeFrom("$apiUrl/todos")
            parameter("action", action)
            parameter("author_id", authorId)
            parameter("project_id", projectId)
            parameter("state", state)
            parameter("type", targetType)
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun markPendingTodoAsDone(
        id: Long
    ): Todo = httpClient.post("$apiUrl/todos/$id/mark_as_done")

    override suspend fun markAllPendingTodosAsDone() {
        httpClient.post<Unit>("$apiUrl/todos/mark_as_done")
    }

    override suspend fun getIssueNotes(
        projectId: Long,
        issueId: Long,
        sort: Sort?,
        orderBy: OrderBy?,
        page: Int,
        pageSize: Int
    ): List<Note> = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$projectId/issues/$issueId/notes")
            parameter("sort", sort)
            parameter("order_by", orderBy)
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun getMergeRequestNotes(
        projectId: Long,
        mergeRequestId: Long,
        sort: Sort?,
        orderBy: OrderBy?,
        page: Int,
        pageSize: Int
    ): List<Note> = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$projectId/merge_requests/$mergeRequestId/notes")
            parameter("sort", sort)
            parameter("order_by", orderBy)
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun createIssueNote(
        projectId: Long,
        issueId: Long,
        body: String
    ): Note = httpClient.post("$apiUrl/projects/$projectId/issues/$issueId/notes") {
        this.body = FormDataContent(Parameters.build {
            append("body", body)
        })
    }

    override suspend fun createMergeRequestNote(
        projectId: Long,
        mergeRequestId: Long,
        body: String
    ): Note = httpClient.post("$apiUrl/projects/$projectId/merge_requests/$mergeRequestId/notes") {
        this.body = FormDataContent(Parameters.build {
            append("body", body)
        })
    }

    override suspend fun getMergeRequestCommits(
        projectId: Long,
        mergeRequestId: Long,
        page: Int,
        pageSize: Int
    ): List<Commit> = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$projectId/merge_requests/$mergeRequestId/commits")
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun getMergeRequestParticipants(
        projectId: Long,
        mergeRequestId: Long,
        page: Int,
        pageSize: Int
    ): List<ShortUser> = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$projectId/merge_requests/$mergeRequestId/participants")
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun getMergeRequestDiffDataList(
        projectId: Long,
        mergeRequestId: Long
    ): MergeRequest = httpClient.get("$apiUrl/projects/$projectId/merge_requests/$mergeRequestId/changes")

    override suspend fun getMilestones(
        projectId: Long,
        state: MilestoneState?,
        page: Int,
        pageSize: Int
    ): List<Milestone> = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$projectId/milestones")
            parameter("state", state)
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun getMilestone(
        projectId: Long,
        mileStoneId: Long
    ): Milestone = httpClient.get("$apiUrl/projects/$projectId/milestones/$mileStoneId")

    override suspend fun createMilestone(
        projectId: Long,
        title: String,
        description: String?,
        dueDate: Date?,
        startDate: Date?
    ): Milestone = httpClient.post("$apiUrl/projects/$projectId/milestones") {
        body = FormDataContent(Parameters.build {
            append("title", title)
            description?.let { append("description", it) }
            dueDate?.let { append("due_date", it.toString()) }
            startDate?.let { append("start_date", it.toString()) }
        })
    }

    override suspend fun updateMilestone(
        projectId: Long,
        mileStoneId: Long,
        title: String?,
        description: String?,
        dueDate: Date?,
        startDate: Date?
    ): Milestone = httpClient.put("$apiUrl/projects/$projectId/milestones/$mileStoneId") {
        body = FormDataContent(Parameters.build {
            title?.let { append("title", it) }
            description?.let { append("description", it) }
            dueDate?.let { append("due_date", it.toString()) }
            startDate?.let { append("start_date", it.toString()) }
        })
    }

    override suspend fun deleteMilestone(
        projectId: Long,
        mileStoneId: Long
    ) {
        httpClient.delete<Unit>("$apiUrl/projects/$projectId/milestones/$mileStoneId")
    }

    override suspend fun getMilestoneIssues(
        projectId: Long,
        mileStoneId: Long,
        page: Int,
        pageSize: Int
    ): List<Issue> = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$projectId/milestones/$mileStoneId/issues")
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun getMilestoneMergeRequests(
        projectId: Long,
        mileStoneId: Long,
        page: Int,
        pageSize: Int
    ): List<MergeRequest> = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$projectId/milestones/$mileStoneId/merge_requests")
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun getProjectLabels(
        projectId: Long,
        page: Int,
        pageSize: Int
    ): List<Label> = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$projectId/labels")
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun createLabel(
        projectId: Long,
        name: String,
        color: String,
        description: String?,
        priority: Int?
    ): Label = httpClient.post("$apiUrl/projects/$projectId/labels") {
        body = FormDataContent(Parameters.build {
            append("name", name)
            append("color", color)
            description?.let { append("expires_at", it) }
            priority?.let { append("expires_at", it.toString()) }
        })
    }

    override suspend fun deleteLabel(
        projectId: Long,
        name: String
    ) {
        httpClient.delete<Unit>("$apiUrl/projects/$projectId/labels") {
            body = FormDataContent(Parameters.build {
                append("name", name)
            })
        }
    }

    override suspend fun subscribeToLabel(
        projectId: Long,
        labelId: Long
    ): Label = httpClient.post("$apiUrl/projects/$projectId/labels/$labelId/subscribe")

    override suspend fun unsubscribeFromLabel(
        projectId: Long,
        labelId: Long
    ): Label = httpClient.post("$apiUrl/projects/$projectId/labels/$labelId/unsubscribe")

    override suspend fun getMyAssignedMergeRequestCount(
        scope: MergeRequestScope,
        state: MergeRequestState,
        pageSize: Int
    ): Int {
        val response = httpClient.head<HttpResponse> {
            url {
                takeFrom("$apiUrl/merge_requests")
                parameter("scope", scope)
                parameter("state", state)
                parameter("per_page", pageSize)
            }
        }
        return response.headers["X-Total"]?.toInt() ?: 0
    }

    override suspend fun getMyAssignedIssueCount(
        scope: IssueScope,
        state: IssueState,
        pageSize: Int
    ): Int {
        val response = httpClient.head<HttpResponse> {
            url {
                takeFrom("$apiUrl/issues")
                parameter("scope", scope)
                parameter("state", state)
                parameter("per_page", pageSize)
            }
        }
        return response.headers["X-Total"]?.toInt() ?: 0
    }

    override suspend fun getMyAssignedTodoCount(
        state: TodoState,
        pageSize: Int
    ): Int {
        val response = httpClient.head<HttpResponse> {
            url {
                takeFrom("$apiUrl/todos")
                parameter("state", state)
                parameter("per_page", pageSize)
            }
        }
        return response.headers["X-Total"]?.toInt() ?: 0
    }

    override suspend fun getMembers(
        projectId: Long,
        page: Int,
        pageSize: Int
    ): List<Member> = httpClient.get {
        url {
            takeFrom("$apiUrl/projects/$projectId/members")
            parameter("page", page)
            parameter("per_page", pageSize)
        }
    }

    override suspend fun getMember(
        projectId: Long,
        memberId: Long
    ): Member = httpClient.get("$apiUrl/projects/$projectId/members/$memberId")

    override suspend fun addMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String?
    ) {
        httpClient.post<Unit>("$apiUrl/projects/$projectId/members") {
            body = FormDataContent(Parameters.build {
                append("user_id", userId.toString())
                append("access_level", accessLevel.toString())
                expiresDate?.let { append("expires_at", it) }
            })
        }
    }

    override suspend fun editMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String?
    ) {
        httpClient.put<Unit>("$apiUrl/projects/$projectId/members/$userId") {
            body = FormDataContent(Parameters.build {
                append("access_level", accessLevel.toString())
                expiresDate?.let { append("expires_at", expiresDate) }
            })
        }
    }

    override suspend fun deleteMember(
        projectId: Long,
        userId: Long
    ) {
        httpClient.delete<Unit>("$apiUrl/projects/$projectId/members/$userId")
    }
}