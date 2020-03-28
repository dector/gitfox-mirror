package ru.terrakok.gitlabclient.presentation.main

import gitfox.model.interactor.AccountInteractor
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moxy.InjectViewState
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.05.19.
 */
@InjectViewState
class MainPresenter @Inject constructor(
    private val accountInteractor: AccountInteractor
) : BasePresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        accountInteractor.getAccountMainBadges()
            .onEach { viewState.setAssignedNotifications(it) }
            .catch { /*do nothing*/ }
            .launchIn(this)
    }
}
