package ru.terrakok.gitlabclient.model.repository.account

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function3
import ru.terrakok.gitlabclient.entity.app.AccountMainBadges
import ru.terrakok.gitlabclient.extension.getXTotalHeader
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.AccountMainBadgesStateModel
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val api: GitlabApi,
    private val badgesStateModel: AccountMainBadgesStateModel,
    private val schedulers: SchedulersProvider
) {

    fun getAccountMainBadges(): Observable<AccountMainBadges> =
        badgesStateModel.observable
            .startWith(refreshAllBadges().onErrorComplete().toObservable())

    fun refreshAllBadges(): Completable =
        Single
            .zip<Int, Int, Int, AccountMainBadges>(
                api.getMyAssignedIssueHeaders().map { it.getXTotalHeader() },
                api.getMyAssignedMergeRequestHeaders().map { it.getXTotalHeader() },
                api.getMyAssignedTodoHeaders().map { it.getXTotalHeader() },
                Function3<Int, Int, Int, AccountMainBadges> { t1, t2, t3 ->
                    AccountMainBadges(t1, t2, t3)
                }
            )
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSuccess { badgesStateModel.accept(it) }
            .ignoreElement()

    fun refreshIssueCount(): Completable =
        api
            .getMyAssignedIssueHeaders()
            .map { it.getXTotalHeader() }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSuccess { badgesStateModel.acceptIssueCount(it) }
            .ignoreElement()

    fun refreshMrCount(): Completable =
        api
            .getMyAssignedMergeRequestHeaders()
            .map { it.getXTotalHeader() }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSuccess { badgesStateModel.acceptMrCount(it) }
            .ignoreElement()

    fun refreshTodoCount(): Completable =
        api
            .getMyAssignedTodoHeaders()
            .map { it.getXTotalHeader() }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
            .doOnSuccess { badgesStateModel.acceptTodoCount(it) }
            .ignoreElement()
}