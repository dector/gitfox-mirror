package ru.terrakok.gitlabclient.model.data.server

import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import retrofit2.Response
import retrofit2.http.*
import ru.terrakok.gitlabclient.entity.*

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 28.03.17
 */
interface GitlabApi {

    companion object {
        const val API_PATH = "api/v4"
        // See GitLab documentation: https://docs.gitlab.com/ee/api/#pagination.
        const val MAX_PAGE_SIZE = 100
    }

    @GET("$API_PATH/projects")
    suspend fun getProjects(
        @Query("archived") archived: Boolean?,
        @Query("visibility") visibility: Visibility?,
        @Query("order_by") orderBy: OrderBy?,
        @Query("sort") sort: Sort?,
        @Query("search") search: String?,
        @Query("simple") simple: Boolean?,
        @Query("owned") owned: Boolean?,
        @Query("membership") membership: Boolean?,
        @Query("starred") starred: Boolean?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<Project>

    @GET("$API_PATH/projects/{id}")
    suspend fun getProject(
        @Path("id") id: Long,
        @Query("statistics") statistics: Boolean = true
    ): Project

    @GET("$API_PATH/projects/{id}/repository/files/{file_path}")
    suspend fun getFile(
        @Path("id") id: Long,
        @Path("file_path") filePath: String,
        @Query("ref") ref: String
    ): File

    @GET("$API_PATH/projects/{id}/repository/tree")
    suspend fun getRepositoryTree(
        @Path("id") id: Long,
        @Query("path") path: String?,
        @Query("ref") branchName: String?,
        @Query("recursive") recursive: Boolean? = false,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<RepositoryTreeNode>

    @GET("$API_PATH/projects/{project_id}/repository/commits/{sha}")
    suspend fun getRepositoryCommit(
        @Path("project_id") projectId: Long,
        @Path("sha") sha: String,
        @Query("stats") stats: Boolean = true
    ): Commit

    @GET("$API_PATH/projects/{project_id}/repository/commits/{sha}/diff")
    suspend fun getCommitDiffData(
        @Path("project_id") projectId: Long,
        @Path("sha") sha: String
    ): List<DiffData>

    @GET("$API_PATH/projects/{project_id}/repository/commits/")
    suspend fun getRepositoryCommits(
        @Path("project_id") projectId: Long,
        @Query("ref_name") branchName: String?,
        @Query("since") since: String?,
        @Query("until") until: String?,
        @Query("path") path: String?,
        @Query("all") all: Boolean?,
        @Query("with_stats") withStats: Boolean?
    ): List<Commit>

    @GET("$API_PATH/projects/{project_id}/repository/branches/")
    suspend fun getRepositoryBranches(
        @Path("project_id") projectId: Long
    ): List<Branch>

    @GET("$API_PATH/user")
    suspend fun getMyUser(): User

    @GET("$API_PATH/issues")
    suspend fun getMyIssues(
        @Query("scope") scope: IssueScope?,
        @Query("state") state: IssueState?,
        @Query("labels") labels: String?,
        @Query("milestone") milestone: String?,
        @Query("iids") iids: Array<Long>?,
        @Query("order_by") orderBy: OrderBy?,
        @Query("sort") sort: Sort?,
        @Query("search") search: String?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<Issue>

    @GET("$API_PATH/projects/{project_id}/issues")
    suspend fun getIssues(
        @Path("project_id") projectId: Long,
        @Query("scope") scope: IssueScope?,
        @Query("state") state: IssueState?,
        @Query("labels") labels: String?,
        @Query("milestone") milestone: String?,
        @Query("iids") iids: Array<Long>?,
        @Query("order_by") orderBy: OrderBy?,
        @Query("sort") sort: Sort?,
        @Query("search") search: String?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<Issue>

    @GET("$API_PATH/projects/{project_id}/issues/{issue_id}")
    suspend fun getIssue(
        @Path("project_id") projectId: Long,
        @Path("issue_id") issueId: Long
    ): Issue

    @FormUrlEncoded
    @PUT("$API_PATH/projects/{project_id}/issues/{issue_id}")
    suspend fun editIssue(
        @Path("project_id") projectId: Long,
        @Path("issue_id") issueId: Long,
        @Field("state_event") stateEvent: IssueStateEvent
    )

    @GET("$API_PATH/events")
    suspend fun getEvents(
        @Query("action") action: EventAction?,
        @Query("target_type") targetType: EventTarget?,
        @Query("before") beforeDay: String?,
        @Query("after") afterDay: String?,
        @Query("sort") sort: Sort?,
        @Query("order_by") orderBy: OrderBy?,
        @Query("scope") scope: EventScope?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<Event>

    @GET("$API_PATH/projects/{project_id}/events")
    suspend fun getProjectEvents(
        @Path("project_id") projectId: Long,
        @Query("action") action: EventAction?,
        @Query("target_type") targetType: EventTarget?,
        @Query("before") beforeDay: String?,
        @Query("after") afterDay: String?,
        @Query("sort") sort: Sort?,
        @Query("order_by") orderBy: OrderBy?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<Event>

    @GET("$API_PATH/merge_requests")
    suspend fun getMyMergeRequests(
        @Query("state") state: MergeRequestState?,
        @Query("milestone") milestone: String?,
        @Query("view") viewType: MergeRequestViewType?,
        @Query("labels") labels: String?,
        @Query("created_before") createdBefore: ZonedDateTime?,
        @Query("created_after") createdAfter: ZonedDateTime?,
        @Query("scope") scope: MergeRequestScope?,
        @Query("author_id") authorId: Int?,
        @Query("assignee_id") assigneeId: Int?,
        @Query("my_reaction_emoji") meReactionEmoji: String?,
        @Query("order_by") orderBy: OrderBy?,
        @Query("sort") sort: Sort?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<MergeRequest>

    @GET("$API_PATH/projects/{project_id}/merge_requests")
    suspend fun getMergeRequests(
        @Path("project_id") projectId: Long,
        @Query("state") state: MergeRequestState?,
        @Query("milestone") milestone: String?,
        @Query("view") viewType: MergeRequestViewType?,
        @Query("labels") labels: String?,
        @Query("created_before") createdBefore: ZonedDateTime?,
        @Query("created_after") createdAfter: ZonedDateTime?,
        @Query("scope") scope: MergeRequestScope?,
        @Query("author_id") authorId: Int?,
        @Query("assignee_id") assigneeId: Int?,
        @Query("my_reaction_emoji") meReactionEmoji: String?,
        @Query("order_by") orderBy: OrderBy?,
        @Query("sort") sort: Sort?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<MergeRequest>

    @GET("$API_PATH/projects/{project_id}/merge_requests/{merge_request_id}")
    suspend fun getMergeRequest(
        @Path("project_id") projectId: Long,
        @Path("merge_request_id") mergeRequestId: Long
    ): MergeRequest

    @GET("$API_PATH/users/{user_id}")
    suspend fun getUser(
        @Path("user_id") userId: Long
    ): User

    @GET("$API_PATH/todos")
    suspend fun getTodos(
        @Query("action") action: TodoAction?,
        @Query("author_id") authorId: Long?,
        @Query("project_id") projectId: Long?,
        @Query("state") state: TodoState?,
        @Query("type") targetType: TargetType?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<Todo>

    @POST("$API_PATH/todos/{id}/mark_as_done")
    suspend fun markPendingTodoAsDone(
        @Path("id") id: Long
    ): Todo

    @POST("$API_PATH/todos/mark_as_done")
    suspend fun markAllPendingTodosAsDone()

    @GET("$API_PATH/projects/{project_id}/issues/{issue_id}/notes")
    suspend fun getIssueNotes(
        @Path("project_id") projectId: Long,
        @Path("issue_id") issueId: Long,
        @Query("sort") sort: Sort?,
        @Query("order_by") orderBy: OrderBy?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<Note>

    @GET("$API_PATH/projects/{project_id}/merge_requests/{merge_request_id}/notes")
    suspend fun getMergeRequestNotes(
        @Path("project_id") projectId: Long,
        @Path("merge_request_id") mergeRequestId: Long,
        @Query("sort") sort: Sort?,
        @Query("order_by") orderBy: OrderBy?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<Note>

    @FormUrlEncoded
    @POST("$API_PATH/projects/{project_id}/issues/{issue_id}/notes")
    suspend fun createIssueNote(
        @Path("project_id") projectId: Long,
        @Path("issue_id") issueId: Long,
        @Field("body") body: String
    ): Note

    @FormUrlEncoded
    @POST("$API_PATH/projects/{project_id}/merge_requests/{merge_request_id}/notes")
    suspend fun createMergeRequestNote(
        @Path("project_id") projectId: Long,
        @Path("merge_request_id") mergeRequestId: Long,
        @Field("body") body: String
    ): Note

    @GET("$API_PATH/projects/{project_id}/merge_requests/{merge_request_id}/commits")
    suspend fun getMergeRequestCommits(
        @Path("project_id") projectId: Long,
        @Path("merge_request_id") mergeRequestId: Long,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<Commit>

    @GET("$API_PATH/projects/{project_id}/merge_requests/{merge_request_id}/participants")
    suspend fun getMergeRequestParticipants(
        @Path("project_id") projectId: Long,
        @Path("merge_request_id") mergeRequestId: Long,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<ShortUser>

    @GET("$API_PATH/projects/{project_id}/merge_requests/{merge_request_id}/changes")
    suspend fun getMergeRequestDiffDataList(
        @Path("project_id") projectId: Long,
        @Path("merge_request_id") mergeRequestId: Long
    ): MergeRequest

    @GET("$API_PATH/projects/{project_id}/milestones")
    suspend fun getMilestones(
        @Path("project_id") projectId: Long,
        @Query("state") state: MilestoneState?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<Milestone>

    @GET("$API_PATH/projects/{project_id}/milestones/{milestone_id}")
    suspend fun getMilestone(
        @Path("project_id") projectId: Long,
        @Path("milestone_id") mileStoneId: Long
    ): Milestone

    @FormUrlEncoded
    @POST("$API_PATH/projects/{project_id}/milestones")
    suspend fun createMilestone(
        @Path("project_id") projectId: Long,
        @Field("title") title: String,
        @Field("description") description: String?,
        @Field("due_date") dueDate: LocalDate?,
        @Field("start_date") startDate: LocalDate?
    ): Milestone

    @FormUrlEncoded
    @PUT("$API_PATH/projects/{project_id}/milestones/{milestone_id}")
    suspend fun updateMilestone(
        @Path("project_id") projectId: Long,
        @Path("milestone_id") mileStoneId: Long,
        @Field("title") title: String?,
        @Field("description") description: String?,
        @Field("due_date") dueDate: LocalDate?,
        @Field("start_date") startDate: LocalDate?
    ): Milestone

    @DELETE("$API_PATH/projects/{project_id}/milestones/{milestone_id}")
    suspend fun deleteMilestone(
        @Path("project_id") projectId: Long,
        @Path("milestone_id") mileStoneId: Long
    )

    @GET("$API_PATH/projects/{project_id}/milestones/{milestone_id}/issues")
    suspend fun getMilestoneIssues(
        @Path("project_id") projectId: Long,
        @Path("milestone_id") mileStoneId: Long,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<Issue>

    @GET("$API_PATH/projects/{project_id}/milestones/{milestone_id}/merge_requests")
    suspend fun getMilestoneMergeRequests(
        @Path("project_id") projectId: Long,
        @Path("milestone_id") mileStoneId: Long,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<MergeRequest>

    @GET("$API_PATH/projects/{project_id}/labels")
    suspend fun getProjectLabels(
        @Path("project_id") projectId: Long,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<Label>

    @FormUrlEncoded
    @POST("$API_PATH/projects/{project_id}/labels")
    suspend fun createLabel(
        @Path("project_id") projectId: Long,
        @Field("name") name: String,
        @Field("color") color: String,
        @Field("description") description: String?,
        @Field("priority") priority: Int?
    ): Label

    @FormUrlEncoded
    @DELETE("$API_PATH/projects/{project_id}/labels")
    suspend fun deleteLabel(
        @Path("project_id") projectId: Long,
        @Field("name") name: String
    )

    @POST("$API_PATH/projects/{project_id}/labels/{label_id}/subscribe")
    suspend fun subscribeToLabel(
        @Path("project_id") projectId: Long,
        @Path("label_id") labelId: Long
    ): Label

    @POST("$API_PATH/projects/{project_id}/labels/{label_id}/unsubscribe")
    suspend fun unsubscribeFromLabel(
        @Path("project_id") projectId: Long,
        @Path("label_id") labelId: Long
    ): Label

    @HEAD("$API_PATH/merge_requests")
    suspend fun getMyAssignedMergeRequestHeaders(
        @Query("scope") scope: MergeRequestScope = MergeRequestScope.ASSIGNED_TO_ME,
        @Query("state") state: MergeRequestState = MergeRequestState.OPENED,
        @Query("per_page") pageSize: Int = 1
    ): Response<Void>

    @HEAD("$API_PATH/issues")
    suspend fun getMyAssignedIssueHeaders(
        @Query("scope") scope: IssueScope = IssueScope.ASSIGNED_BY_ME,
        @Query("state") state: IssueState = IssueState.OPENED,
        @Query("per_page") pageSize: Int = 1
    ): Response<Void>

    @HEAD("$API_PATH/todos")
    suspend fun getMyAssignedTodoHeaders(
        @Query("state") state: TodoState = TodoState.PENDING,
        @Query("per_page") pageSize: Int = 1
    ): Response<Void>

    @GET("$API_PATH/projects/{project_id}/members")
    suspend fun getMembers(
        @Path("project_id") projectId: Long,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): List<Member>

    @GET("$API_PATH/projects/{project_id}/members/{member_id}")
    suspend fun getMember(
        @Path("project_id") projectId: Long,
        @Path("member_id") memberId: Long
    ): Member

    @POST("$API_PATH/projects/{project_id}/members")
    suspend fun addMember(
        @Path("project_id") projectId: Long,
        @Field("user_id") userId: Long,
        @Field("access_level") accessLevel: Long,
        @Field("expires_at") expiresDate: String?
    )

    @PUT("$API_PATH/projects/{project_id}/members/{user_id}")
    suspend fun editMember(
        @Path("project_id") projectId: Long,
        @Path("user_id") userId: Long,
        @Field("access_level") accessLevel: Long,
        @Field("expires_at") expiresDate: String?
    )

    @DELETE("$API_PATH/projects/{project_id}/members/{user_id}")
    suspend fun deleteMember(
        @Path("project_id") projectId: Long,
        @Path("user_id") userId: Long
    )
}
