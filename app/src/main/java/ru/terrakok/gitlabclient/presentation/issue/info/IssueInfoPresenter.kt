package ru.terrakok.gitlabclient.presentation.issue.info

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.model.interactor.issue.IssueInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.IssueId
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 05.01.18.
 */
@InjectViewState
class IssueInfoPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    @IssueId private val issueIdWrapper: PrimitiveWrapper<Long>,
    private val issueInteractor: IssueInteractor,
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler
) : BasePresenter<IssueInfoView>() {

    private val projectId = projectIdWrapper.value
    private val issueId = issueIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        issueInteractor
            .getIssue(projectId, issueId)
            .flatMap { issue ->
                mdConverter
                    .markdownToSpannable(issue.description)
                    .map { Pair(issue, it) }
            }
            .doOnSubscribe { viewState.showEmptyProgress(true) }
            .doAfterTerminate { viewState.showEmptyProgress(false) }
            .subscribe(
                { (issue, mdDescription) -> viewState.showInfo(issue, mdDescription) },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
    }
}