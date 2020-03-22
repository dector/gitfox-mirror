package ru.terrakok.gitlabclient.model.interactor

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import retrofit2.Response
import ru.terrakok.gitlabclient.entity.*
import ru.terrakok.gitlabclient.entity.app.AccountMainBadges
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges

class AccountInteractor(
    private val serverPath: String,
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val todoInteractor: TodoInteractor,
    private val mrInteractor: MergeRequestInteractor,
    private val issueInteractor: IssueInteractor
) {

    private val issueCount: Flow<Int> =
        serverChanges.issueChanges
            .map { api.getMyAssignedIssueHeaders().getXTotalHeader() }

    private val mrCount: Flow<Int> =
        serverChanges.mergeRequestChanges
            .map { api.getMyAssignedMergeRequestHeaders().getXTotalHeader() }

    private val todoCount: Flow<Int> =
        serverChanges.todoChanges
            .map { api.getMyAssignedTodoHeaders().getXTotalHeader() }

    fun getAccountMainBadges(): Flow<AccountMainBadges> =
        combine(issueCount, mrCount, todoCount) { i, mr, t -> AccountMainBadges(i, mr, t) }
            .onStart {
                emit(
                    AccountMainBadges(
                        api.getMyAssignedIssueHeaders().getXTotalHeader(),
                        api.getMyAssignedMergeRequestHeaders().getXTotalHeader(),
                        api.getMyAssignedTodoHeaders().getXTotalHeader()
                    )
                )
            }

    suspend fun getMyProfile(): User = api.getMyUser()

    fun getMyServerName(): String = serverPath

    suspend fun getMyTodos(
        isPending: Boolean,
        page: Int
    ): List<TargetHeader> {
        val me = getMyProfile()
        return todoInteractor.getTodos(
            currentUser = me,
            state = if (isPending) TodoState.PENDING else TodoState.DONE,
            page = page
        )
    }

    suspend fun getMyMergeRequests(
        createdByMe: Boolean,
        onlyOpened: Boolean,
        page: Int
    ): List<TargetHeader> =
        mrInteractor.getMyMergeRequests(
            scope = if (createdByMe) MergeRequestScope.CREATED_BY_ME else MergeRequestScope.ASSIGNED_TO_ME,
            state = if (onlyOpened) MergeRequestState.OPENED else null,
            orderBy = OrderBy.UPDATED_AT,
            page = page
        )

    suspend fun getMyIssues(
        createdByMe: Boolean,
        onlyOpened: Boolean,
        page: Int
    ): List<TargetHeader> =
        issueInteractor.getMyIssues(
            scope = if (createdByMe) IssueScope.CREATED_BY_ME else IssueScope.ASSIGNED_BY_ME,
            state = if (onlyOpened) IssueState.OPENED else null,
            orderBy = OrderBy.UPDATED_AT,
            page = page
        )

    private fun Response<*>.getXTotalHeader(): Int =
        if (isSuccessful) headers().get("X-Total")?.toInt() ?: 0 else 0
}
