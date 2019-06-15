package ru.terrakok.gitlabclient.model.repository.account

import io.reactivex.Observable
import io.reactivex.functions.Function3
import retrofit2.adapter.rxjava2.Result
import ru.terrakok.gitlabclient.entity.app.AccountMainBadges
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val schedulers: SchedulersProvider
) {

    private val issueCount =
        serverChanges.issueChanges
            .map { Unit }
            .startWith(Unit)
            .switchMapSingle {
                api.getMyAssignedIssueHeaders()
                    .map { it.getXTotalHeader() }
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
            }

    private val mrCount =
        serverChanges.mergeRequestChanges
            .map { Unit }
            .startWith(Unit)
            .switchMapSingle {
                api.getMyAssignedMergeRequestHeaders()
                    .map { it.getXTotalHeader() }
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
            }

    private val todoCount =
        serverChanges.todoChanges
            .map { Unit }
            .startWith(Unit)
            .switchMapSingle {
                api.getMyAssignedTodoHeaders()
                    .map { it.getXTotalHeader() }
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
            }

    fun getAccountMainBadges(): Observable<AccountMainBadges> =
        Observable.zip(
            issueCount, mrCount, todoCount,
            Function3 { i, mr, t -> AccountMainBadges(i, mr, t) }
        )

    private fun Result<*>.getXTotalHeader(): Int {
        return if (!isError) response().headers().get("X-Total")?.toInt() ?: 0 else 0
    }
}