package ru.terrakok.gitlabclient.presentation.issue.details

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.di.IssueId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.model.interactor.issue.IssueInteractor
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

        issueInteractor
            .getIssue(projectId, issueId)
            .doOnSubscribe { viewState.showEmptyProgress(true) }
            .doAfterTerminate { viewState.showEmptyProgress(false) }
            .subscribe(
                { issue -> viewState.showDetails(issue) },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
    }
}