package ru.terrakok.gitlabclient.presentation.issue.details

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.di.IssueId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.model.interactor.IssueInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 26.05.19.
 */
@InjectViewState
class IssueDetailsPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    @IssueId private val issueIdWrapper: PrimitiveWrapper<Long>,
    private val issueInteractor: IssueInteractor,
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler
) : BasePresenter<IssueDetailsView>() {

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
            val spnbl = mdConverter.toSpannable(issue.description)
            viewState.showDetails(issue, spnbl)
        } catch (e: Exception) {
            errorHandler.proceed(e) { viewState.showMessage(it) }
        }
        viewState.showEmptyProgress(false)
    }
}
