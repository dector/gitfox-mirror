package ru.terrakok.gitlabclient.model.interactor

import io.reactivex.Observable
import io.reactivex.functions.Function3
import kotlinx.coroutines.rx2.rxSingle
import retrofit2.Response
import ru.terrakok.gitlabclient.di.ServerPath
import ru.terrakok.gitlabclient.entity.*
import ru.terrakok.gitlabclient.entity.app.AccountMainBadges
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

class AccountInteractor @Inject constructor(
    @ServerPath private val serverPath: String,
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val todoInteractor: TodoInteractor,
    private val mrInteractor: MergeRequestInteractor,
    private val issueInteractor: IssueInteractor,
    private val schedulers: SchedulersProvider
) {

    private val issueCount =
        serverChanges.issueChanges
            .map { Unit }
            .startWith(Unit)
            .switchMapSingle {
                rxSingle { api.getMyAssignedIssueHeaders() }
                    .map { it.getXTotalHeader() }
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
            }

    private val mrCount =
        serverChanges.mergeRequestChanges
            .map { Unit }
            .startWith(Unit)
            .switchMapSingle {
                rxSingle { api.getMyAssignedMergeRequestHeaders() }
                    .map { it.getXTotalHeader() }
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
            }

    private val todoCount =
        serverChanges.todoChanges
            .map { Unit }
            .startWith(Unit)
            .switchMapSingle {
                rxSingle { api.getMyAssignedTodoHeaders() }
                    .map { it.getXTotalHeader() }
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
            }

    fun getAccountMainBadges(): Observable<AccountMainBadges> =
        Observable.zip(
            issueCount, mrCount, todoCount,
            Function3 { i, mr, t -> AccountMainBadges(i, mr, t) }
        )

    fun getMyProfile() =
        rxSingle { api.getMyUser() }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getMyServerName() = serverPath

    fun getMyTodos(
        isPending: Boolean,
        page: Int
    ) = getMyProfile()
        .flatMap { currentUser ->
            todoInteractor.getTodos(
                currentUser = currentUser,
                state = if (isPending) TodoState.PENDING else TodoState.DONE,
                page = page
            )
        }

    fun getMyMergeRequests(
        createdByMe: Boolean,
        onlyOpened: Boolean,
        page: Int
    ) =
        mrInteractor.getMyMergeRequests(
            scope = if (createdByMe) MergeRequestScope.CREATED_BY_ME else MergeRequestScope.ASSIGNED_TO_ME,
            state = if (onlyOpened) MergeRequestState.OPENED else null,
            orderBy = OrderBy.UPDATED_AT,
            page = page
        )

    fun getMyIssues(
        createdByMe: Boolean,
        onlyOpened: Boolean,
        page: Int
    ) =
        issueInteractor.getMyIssues(
            scope = if (createdByMe) IssueScope.CREATED_BY_ME else IssueScope.ASSIGNED_BY_ME,
            state = if (onlyOpened) IssueState.OPENED else null,
            orderBy = OrderBy.UPDATED_AT,
            page = page
        )

    private fun Response<*>.getXTotalHeader(): Int {
        return if (isSuccessful) headers().get("X-Total")?.toInt() ?: 0 else 0
    }
}
