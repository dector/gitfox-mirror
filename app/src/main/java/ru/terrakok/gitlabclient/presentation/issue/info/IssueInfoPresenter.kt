package ru.terrakok.gitlabclient.presentation.issue.info

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import ru.terrakok.gitlabclient.di.IssueId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.issue.Issue
import ru.terrakok.gitlabclient.model.interactor.IssueInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import javax.inject.Inject

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
            .switchMapSingle { getIssue() }
            .subscribe(
                { viewState.showInfo(it) },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
    }

    private fun getIssue(): Single<Issue> {
        return issueInteractor.getIssue(projectId, issueId)
            .doOnSubscribe { viewState.showEmptyProgress(true) }
            .doAfterTerminate { viewState.showEmptyProgress(false) }
    }
}