package ru.terrakok.gitlabclient.presentation.issue.info

import javax.inject.Inject
import moxy.InjectViewState
import ru.terrakok.gitlabclient.di.IssueId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.model.interactor.IssueInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 05.01.18.
 */
@InjectViewState
class IssueInfoPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    @IssueId private val issueIdWrapper: PrimitiveWrapper<Long>,
    private val issueInteractor: IssueInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<IssueInfoView>() {

    private val projectId = projectIdWrapper.value
    private val issueId = issueIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        issueInteractor
            .issueChanges
            .startWith(issueId)
            .filter { it == issueId }
            .switchMapMaybe {
                issueInteractor.getIssue(projectId, issueId)
                    .toMaybe()
                    .doOnSubscribe { viewState.showEmptyProgress(true) }
                    .doAfterTerminate { viewState.showEmptyProgress(false) }
                    .doOnSuccess { viewState.showInfo(it) }
                    .doOnError { errorHandler.proceed(it, { viewState.showMessage(it) }) }
                    .onErrorComplete()
            }
            .subscribe()
            .connect()
    }
}
