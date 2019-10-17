package ru.terrakok.gitlabclient.presentation.issue.details

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import ru.terrakok.gitlabclient.di.IssueId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.issue.Issue
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
        issueInteractor
            .issueChanges
            .startWith(issueId)
            .filter { it == issueId }
            .switchMapMaybe {
                getIssue()
                    .toMaybe()
                    .doOnSubscribe { viewState.showEmptyProgress(true) }
                    .doAfterTerminate { viewState.showEmptyProgress(false) }
                    .doOnSuccess { (issue, mdDescription) -> viewState.showDetails(issue, mdDescription) }
                    .doOnError { errorHandler.proceed(it, { viewState.showMessage(it) }) }
                    .onErrorComplete()
            }
            .subscribe()
            .connect()
    }

    private fun getIssue(): Single<Pair<Issue, CharSequence>> {
        return issueInteractor
            .getIssue(projectId, issueId)
            .flatMap { issue ->
                mdConverter
                    .markdownToSpannable(issue.description)
                    .map { Pair(issue, it) }
            }
    }
}