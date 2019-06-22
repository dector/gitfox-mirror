package ru.terrakok.gitlabclient.model.data.server

import io.reactivex.Completable
import io.reactivex.Single
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.*
import ru.terrakok.gitlabclient.entity.*
import ru.terrakok.gitlabclient.entity.event.Event
import ru.terrakok.gitlabclient.entity.event.EventAction
import ru.terrakok.gitlabclient.entity.event.EventTarget
import ru.terrakok.gitlabclient.entity.issue.Issue
import ru.terrakok.gitlabclient.entity.issue.IssueScope
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestScope
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestViewType
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.entity.milestone.MilestoneState
import ru.terrakok.gitlabclient.entity.target.TargetType
import ru.terrakok.gitlabclient.entity.todo.Todo
import ru.terrakok.gitlabclient.entity.todo.TodoAction
import ru.terrakok.gitlabclient.entity.todo.TodoState

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
    fun getProjects(
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
    ): Single<List<Project>>

    @GET("$API_PATH/projects/{id}")
    fun getProject(
        @Path("id") id: Long,
        @Query("statistics") statistics: Boolean = true
    ): Single<Project>

    @GET("$API_PATH/projects/{id}/repository/files/{file_path}")
    fun getFile(
        @Path("id") id: Long,
        @Path("file_path") filePath: String,
        @Query("ref") ref: String
    ): Single<File>

    @GET("$API_PATH/projects/{id}/repository/tree")
    fun getRepositoryTree(
        @Path("id") id: Long,
        @Query("path") path: String?,
        @Query("ref") branchName: String?,
        @Query("recursive") recursive: Boolean? = false,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<RepositoryTreeNode>>

    @GET("$API_PATH/projects/{project_id}/repository/commits/{sha}")
    fun getRepositoryCommit(
        @Path("project_id") projectId: Long,
        @Path("sha") sha: String,
        @Query("stats") stats: Boolean = true
    ): Single<Commit>

    @GET("$API_PATH/projects/{project_id}/repository/commits/")
    fun getRepositoryCommits(
        @Path("project_id") projectId: Long,
        @Query("ref_name") branchName: String?,
        @Query("since") since: String?,
        @Query("until") until: String?,
        @Query("path") path: String?,
        @Query("all") all: Boolean?,
        @Query("with_stats") withStats: Boolean?
    ): Single<List<Commit>>

    @GET("$API_PATH/projects/{project_id}/repository/branches/")
    fun getRepositoryBranches(
        @Path("project_id") projectId: Long
    ): Single<List<Branch>>

    @GET("$API_PATH/user")
    fun getMyUser(): Single<User>

    @GET("$API_PATH/issues")
    fun getMyIssues(
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
    ): Single<List<Issue>>

    @GET("$API_PATH/projects/{project_id}/issues")
    fun getIssues(
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
    ): Single<List<Issue>>

    @GET("$API_PATH/projects/{project_id}/issues/{issue_id}")
    fun getIssue(
        @Path("project_id") projectId: Long,
        @Path("issue_id") issueId: Long
    ): Single<Issue>

    @GET("$API_PATH/events")
    fun getEvents(
        @Query("action") action: EventAction?,
        @Query("target_type") targetType: EventTarget?,
        @Query("before") beforeDay: String?,
        @Query("after") afterDay: String?,
        @Query("sort") sort: Sort?,
        @Query("order_by") orderBy: OrderBy?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<Event>>

    @GET("$API_PATH/projects/{project_id}/events")
    fun getProjectEvents(
        @Path("project_id") projectId: Long,
        @Query("action") action: EventAction?,
        @Query("target_type") targetType: EventTarget?,
        @Query("before") beforeDay: String?,
        @Query("after") afterDay: String?,
        @Query("sort") sort: Sort?,
        @Query("order_by") orderBy: OrderBy?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<Event>>

    @GET("$API_PATH/merge_requests")
    fun getMyMergeRequests(
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
    ): Single<List<MergeRequest>>

    @GET("$API_PATH/projects/{project_id}/merge_requests")
    fun getMergeRequests(
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
    ): Single<List<MergeRequest>>

    @GET("$API_PATH/projects/{project_id}/merge_requests/{merge_request_id}")
    fun getMergeRequest(
        @Path("project_id") projectId: Long,
        @Path("merge_request_id") mergeRequestId: Long
    ): Single<MergeRequest>

    @GET("$API_PATH/users/{user_id}")
    fun getUser(
        @Path("user_id") userId: Long
    ): Single<User>

    @GET("$API_PATH/todos")
    fun getTodos(
        @Query("action") action: TodoAction?,
        @Query("author_id") authorId: Long?,
        @Query("project_id") projectId: Long?,
        @Query("state") state: TodoState?,
        @Query("type") targetType: TargetType?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<Todo>>

    @POST("$API_PATH/todos/{id}/mark_as_done")
    fun markPendingTodoAsDone(
        @Path("id") id: Long
    ): Single<Todo>

    @POST("$API_PATH/todos/mark_as_done")
    fun markAllPendingTodosAsDone(): Completable

    @GET("$API_PATH/projects/{project_id}/issues/{issue_id}/notes")
    fun getIssueNotes(
        @Path("project_id") projectId: Long,
        @Path("issue_id") issueId: Long,
        @Query("sort") sort: Sort?,
        @Query("order_by") orderBy: OrderBy?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<Note>>

    @GET("$API_PATH/projects/{project_id}/merge_requests/{merge_request_id}/notes")
    fun getMergeRequestNotes(
        @Path("project_id") projectId: Long,
        @Path("merge_request_id") mergeRequestId: Long,
        @Query("sort") sort: Sort?,
        @Query("order_by") orderBy: OrderBy?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<Note>>

    @FormUrlEncoded
    @POST("$API_PATH/projects/{project_id}/issues/{issue_id}/notes")
    fun createIssueNote(
        @Path("project_id") projectId: Long,
        @Path("issue_id") issueId: Long,
        @Field("body") body: String
    ): Single<Note>

    @FormUrlEncoded
    @POST("$API_PATH/projects/{project_id}/merge_requests/{merge_request_id}/notes")
    fun createMergeRequestNote(
        @Path("project_id") projectId: Long,
        @Path("merge_request_id") mergeRequestId: Long,
        @Field("body") body: String
    ): Single<Note>

    @GET("$API_PATH/projects/{project_id}/merge_requests/{merge_request_id}/commits")
    fun getMergeRequestCommits(
        @Path("project_id") projectId: Long,
        @Path("merge_request_id") mergeRequestId: Long,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<Commit>>

    @GET("$API_PATH/projects/{project_id}/merge_requests/{merge_request_id}/participants")
    fun getMergeRequestParticipants(
        @Path("project_id") projectId: Long,
        @Path("merge_request_id") mergeRequestId: Long,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<ShortUser>>

    @GET("$API_PATH/projects/{project_id}/merge_requests/{merge_request_id}/changes")
    fun getMergeRequestChanges(
        @Path("project_id") projectId: Long,
        @Path("merge_request_id") mergeRequestId: Long
    ): Single<MergeRequest>

    @GET("$API_PATH/projects/{project_id}/milestones")
    fun getMilestones(
        @Path("project_id") projectId: Long,
        @Query("state") state: MilestoneState?,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<Milestone>>

    @GET("$API_PATH/projects/{project_id}/milestones/{milestone_id}")
    fun getMilestone(
        @Path("project_id") projectId: Long,
        @Path("milestone_id") mileStoneId: Long
    ): Single<Milestone>

    @FormUrlEncoded
    @POST("$API_PATH/projects/{project_id}/milestones")
    fun createMilestone(
        @Path("project_id") projectId: Long,
        @Field("title") title: String,
        @Field("description") description: String?,
        @Field("due_date") dueDate: LocalDate?,
        @Field("start_date") startDate: LocalDate?
    ): Single<Milestone>

    @FormUrlEncoded
    @PUT("$API_PATH/projects/{project_id}/milestones/{milestone_id}")
    fun updateMilestone(
        @Path("project_id") projectId: Long,
        @Path("milestone_id") mileStoneId: Long,
        @Field("title") title: String?,
        @Field("description") description: String?,
        @Field("due_date") dueDate: LocalDate?,
        @Field("start_date") startDate: LocalDate?
    ): Single<Milestone>

    @DELETE("$API_PATH/projects/{project_id}/milestones/{milestone_id}")
    fun deleteMilestone(
        @Path("project_id") projectId: Long,
        @Path("milestone_id") mileStoneId: Long
    ): Completable

    @GET("$API_PATH/projects/{project_id}/milestones/{milestone_id}/issues")
    fun getMilestoneIssues(
        @Path("project_id") projectId: Long,
        @Path("milestone_id") mileStoneId: Long,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<Issue>>

    @GET("$API_PATH/projects/{project_id}/milestones/{milestone_id}/merge_requests")
    fun getMilestoneMergeRequests(
        @Path("project_id") projectId: Long,
        @Path("milestone_id") mileStoneId: Long,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<MergeRequest>>

    @GET("$API_PATH/projects/{project_id}/labels")
    fun getProjectLabels(
        @Path("project_id") projectId: Long,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<Label>>

    @FormUrlEncoded
    @POST("$API_PATH/projects/{project_id}/labels")
    fun createLabel(
        @Path("project_id") projectId: Long,
        @Field("name") name: String,
        @Field("color") color: String,
        @Field("description") description: String?,
        @Field("priority") priority: Int?
    ): Single<Label>

    @FormUrlEncoded
    @DELETE("$API_PATH/projects/{project_id}/labels")
    fun deleteLabel(
        @Path("project_id") projectId: Long,
        @Field("name") name: String
    ): Completable

    @POST("$API_PATH/projects/{project_id}/labels/{label_id}/subscribe")
    fun subscribeToLabel(
        @Path("project_id") projectId: Long,
        @Path("label_id") labelId: Long
    ): Single<Label>

    @POST("$API_PATH/projects/{project_id}/labels/{label_id}/unsubscribe")
    fun unsubscribeFromLabel(
        @Path("project_id") projectId: Long,
        @Path("label_id") labelId: Long
    ): Single<Label>

    @HEAD("$API_PATH/merge_requests")
    fun getMyAssignedMergeRequestHeaders(
        @Query("scope") scope: MergeRequestScope = MergeRequestScope.ASSIGNED_TO_ME,
        @Query("state") state: MergeRequestState = MergeRequestState.OPENED,
        @Query("per_page") pageSize: Int = 1
    ): Single<Result<Void>>

    @HEAD("$API_PATH/issues")
    fun getMyAssignedIssueHeaders(
        @Query("scope") scope: IssueScope = IssueScope.ASSIGNED_BY_ME,
        @Query("state") state: IssueState = IssueState.OPENED,
        @Query("per_page") pageSize: Int = 1
    ): Single<Result<Void>>

    @HEAD("$API_PATH/todos")
    fun getMyAssignedTodoHeaders(
        @Query("state") state: TodoState = TodoState.PENDING,
        @Query("per_page") pageSize: Int = 1
    ): Single<Result<Void>>

    @GET("$API_PATH/projects/{project_id}/members")
    fun getMembers(
        @Path("project_id") projectId: Long,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): Single<List<Member>>

    @GET("$API_PATH/projects/{project_id}/members/{member_id}")
    fun getMember(
        @Path("project_id") projectId: Long,
        @Path("member_id") memberId: Long
    ): Single<Member>

    @POST("$API_PATH/projects/{project_id}/members")
    fun addMember(
        @Path("project_id") projectId: Long,
        @Field("user_id") userId: Long,
        @Field("access_level") accessLevel: Long,
        @Field("expires_at") expiresDate: String?
    ): Completable

    @PUT("$API_PATH/projects/{project_id}/members/{user_id}")
    fun editMember(
        @Path("project_id") projectId: Long,
        @Path("user_id") userId: Long,
        @Field("access_level") accessLevel: Long,
        @Field("expires_at") expiresDate: String?
    ): Completable

    @DELETE("$API_PATH/projects/{project_id}/members/{user_id}")
    fun deleteMember(
        @Path("project_id") projectId: Long,
        @Path("user_id") userId: Long
    ): Completable
}