package ru.terrakok.gitlabclient.presentation.main

import moxy.InjectViewState
import ru.terrakok.gitlabclient.model.interactor.AccountInteractor
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
            .subscribe(
                { viewState.setAssignedNotifications(it) },
                {
                    // TODO: user activity badges (Maybe we can retry this request, until it finishes correctly?).
                }
            )
            .connect()
    }
}