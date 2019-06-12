package ru.terrakok.gitlabclient.model.interactor.account

import io.reactivex.Observable
import ru.terrakok.gitlabclient.entity.app.AccountMainBadges
import ru.terrakok.gitlabclient.model.repository.account.AccountRepository
import javax.inject.Inject

class AccountInteractor @Inject constructor(
    private val accountRepository: AccountRepository
) {

    fun getAccountMainBadges(): Observable<AccountMainBadges> =
        accountRepository.getAccountMainBadges()
}