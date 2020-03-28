package gitfox.model.data.server

import gitfox.entity.*

internal interface GitlabApi {

    companion object {
        const val API_PATH = "api/v4"

        // See GitLab documentation: https://docs.gitlab.com/ee/api/#pagination.
        const val MAX_PAGE_SIZE = 100
    }

    suspend fun getProjects(
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
    ): List<Project>

    suspend fun getProject(
        id: Long,
        statistics: Boolean = true
    ): Project

    suspend fun getFile(
        id: Long,
        filePath: String,
        ref: String
    ): File

    suspend fun getRepositoryTree(
        id: Long,
        path: String?,
        branchName: String?,
        recursive: Boolean? = false,
        page: Int,
        pageSize: Int
    ): List<RepositoryTreeNode>

    suspend fun getRepositoryCommit(
        projectId: Long,
        sha: String,
        stats: Boolean = true
    ): Commit

    suspend fun getCommitDiffData(
        projectId: Long,
        sha: String
    ): List<DiffData>

    suspend fun getRepositoryCommits(
        projectId: Long,
        branchName: String?,
        since: String?,
        until: String?,
        path: String?,
        all: Boolean?,
        withStats: Boolean?
    ): List<Commit>

    suspend fun getRepositoryBranches(
        projectId: Long
    ): List<Branch>

    suspend fun getMyUser(): User

    suspend fun getMyIssues(
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
    ): List<Issue>

    suspend fun getIssues(
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
    ): List<Issue>

    suspend fun getIssue(
        projectId: Long,
        issueId: Long
    ): Issue

    suspend fun editIssue(
        projectId: Long,
        issueId: Long,
        stateEvent: IssueStateEvent
    )

    suspend fun getEvents(
        action: EventAction?,
        targetType: EventTarget?,
        beforeDay: Date?,
        afterDay: Date?,
        sort: Sort?,
        orderBy: OrderBy?,
        scope: EventScope?,
        page: Int,
        pageSize: Int
    ): List<Event>

    suspend fun getProjectEvents(
        projectId: Long,
        action: EventAction?,
        targetType: EventTarget?,
        beforeDay: Date?,
        afterDay: Date?,
        sort: Sort?,
        orderBy: OrderBy?,
        page: Int,
        pageSize: Int
    ): List<Event>

    suspend fun getMyMergeRequests(
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
    ): List<MergeRequest>

    suspend fun getMergeRequests(
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
    ): List<MergeRequest>

    suspend fun getMergeRequest(
        projectId: Long,
        mergeRequestId: Long
    ): MergeRequest

    suspend fun getUser(
        userId: Long
    ): User

    suspend fun getTodos(
        action: TodoAction?,
        authorId: Long?,
        projectId: Long?,
        state: TodoState?,
        targetType: TargetType?,
        page: Int,
        pageSize: Int
    ): List<Todo>

    suspend fun markPendingTodoAsDone(
        id: Long
    ): Todo

    suspend fun markAllPendingTodosAsDone()

    suspend fun getIssueNotes(
        projectId: Long,
        issueId: Long,
        sort: Sort?,
        orderBy: OrderBy?,
        page: Int,
        pageSize: Int
    ): List<Note>

    suspend fun getMergeRequestNotes(
        projectId: Long,
        mergeRequestId: Long,
        sort: Sort?,
        orderBy: OrderBy?,
        page: Int,
        pageSize: Int
    ): List<Note>

    suspend fun createIssueNote(
        projectId: Long,
        issueId: Long,
        body: String
    ): Note

    suspend fun createMergeRequestNote(
        projectId: Long,
        mergeRequestId: Long,
        body: String
    ): Note

    suspend fun getMergeRequestCommits(
        projectId: Long,
        mergeRequestId: Long,
        page: Int,
        pageSize: Int
    ): List<Commit>

    suspend fun getMergeRequestParticipants(
        projectId: Long,
        mergeRequestId: Long,
        page: Int,
        pageSize: Int
    ): List<ShortUser>

    suspend fun getMergeRequestDiffDataList(
        projectId: Long,
        mergeRequestId: Long
    ): MergeRequest

    suspend fun getMilestones(
        projectId: Long,
        state: MilestoneState?,
        page: Int,
        pageSize: Int
    ): List<Milestone>

    suspend fun getMilestone(
        projectId: Long,
        mileStoneId: Long
    ): Milestone

    suspend fun createMilestone(
        projectId: Long,
        title: String,
        description: String?,
        dueDate: Date?,
        startDate: Date?
    ): Milestone

    suspend fun updateMilestone(
        projectId: Long,
        mileStoneId: Long,
        title: String?,
        description: String?,
        dueDate: Date?,
        startDate: Date?
    ): Milestone

    suspend fun deleteMilestone(
        projectId: Long,
        mileStoneId: Long
    )

    suspend fun getMilestoneIssues(
        projectId: Long,
        mileStoneId: Long,
        page: Int,
        pageSize: Int
    ): List<Issue>

    suspend fun getMilestoneMergeRequests(
        projectId: Long,
        mileStoneId: Long,
        page: Int,
        pageSize: Int
    ): List<MergeRequest>

    suspend fun getProjectLabels(
        projectId: Long,
        page: Int,
        pageSize: Int
    ): List<Label>

    suspend fun createLabel(
        projectId: Long,
        name: String,
        color: String,
        description: String?,
        priority: Int?
    ): Label

    suspend fun deleteLabel(
        projectId: Long,
        name: String
    )

    suspend fun subscribeToLabel(
        projectId: Long,
        labelId: Long
    ): Label

    suspend fun unsubscribeFromLabel(
        projectId: Long,
        labelId: Long
    ): Label

    suspend fun getMyAssignedMergeRequestCount(
        scope: MergeRequestScope = MergeRequestScope.ASSIGNED_TO_ME,
        state: MergeRequestState = MergeRequestState.OPENED,
        pageSize: Int = 1
    ): Int

    suspend fun getMyAssignedIssueCount(
        scope: IssueScope = IssueScope.ASSIGNED_BY_ME,
        state: IssueState = IssueState.OPENED,
        pageSize: Int = 1
    ): Int

    suspend fun getMyAssignedTodoCount(
        state: TodoState = TodoState.PENDING,
        pageSize: Int = 1
    ): Int

    suspend fun getMembers(
        projectId: Long,
        page: Int,
        pageSize: Int
    ): List<Member>

    suspend fun getMember(
        projectId: Long,
        memberId: Long
    ): Member

    suspend fun addMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String?
    )

    suspend fun editMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String?
    )

    suspend fun deleteMember(
        projectId: Long,
        userId: Long
    )
}
