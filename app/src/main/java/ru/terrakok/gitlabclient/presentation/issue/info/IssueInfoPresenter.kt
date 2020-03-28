package ru.terrakok.gitlabclient.presentation.issue.info

import gitfox.model.interactor.IssueInteractor
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.di.IssueId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
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

        launch {
            loadIssue()
            issueInteractor.issueChanges
                .filter { it == issueId }
                .collect { loadIssue() }
        }
    }

    private suspend fun loadIssue() {
        viewState.showEmptyProgress(true)
        try {
            val issue = issueInteractor.getIssue(projectId, issueId)
            viewState.showInfo(issue)
        } catch (e: Exception) {
            errorHandler.proceed(e) { viewState.showMessage(it) }
        }
        viewState.showEmptyProgress(false)
    }
}
