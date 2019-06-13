package ru.terrakok.gitlabclient.model.data.state

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import ru.terrakok.gitlabclient.entity.app.AccountMainBadges

class AccountMainBadgesStateModel {
    private val relay = BehaviorRelay.create<AccountMainBadges>()

    val observable: Observable<AccountMainBadges> = relay.hide()

    fun accept(value: AccountMainBadges) {
        relay.accept(value)
    }

    fun acceptIssueCount(value: Int) {
        relay.value?.let { current ->
            relay.accept(current.copy(issueCount = value))
        }
    }

    fun acceptMrCount(value: Int) {
        relay.value?.let { current ->
            relay.accept(current.copy(mergeRequestCount = value))
        }
    }

    fun acceptTodoCount(value: Int) {
        relay.value?.let { current ->
            relay.accept(current.copy(todoCount = value))
        }
    }
}