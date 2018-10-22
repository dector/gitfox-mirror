package ru.terrakok.gitlabclient.presentation.issue.info

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.issue.Issue
import ru.terrakok.gitlabclient.model.interactor.issue.IssueInteractor
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.ProjectMarkDownConverterProvider
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.IssueId
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import javax.inject.Inject

private typealias IssueLinker = BiFunction<Pair<Issue, CharSequence>, Project, IssueInfoView.IssueInfo>

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 05.01.18.
 */
@InjectViewState
class IssueInfoPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    @IssueId private val issueIdWrapper: PrimitiveWrapper<Long>,
    private val issueInteractor: IssueInteractor,
    private val projectInteractor: ProjectInteractor,
    private val mdConverterProvider: ProjectMarkDownConverterProvider,
    private val errorHandler: ErrorHandler
) : BasePresenter<IssueInfoView>() {

    private val projectId = projectIdWrapper.value
    private val issueId = issueIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        Single
            .zip(
                issueInteractor
                    .getIssue(projectId, issueId)
                    .flatMap { issue ->
                        mdConverterProvider
                            .getMarkdownConverter(issue.projectId)
                            .flatMap { converter ->
                                converter
                                    .markdownToSpannable(issue.description ?: "")
                                    .map { issue to it }
                            }
                    },
                projectInteractor.getProject(projectId),
                IssueLinker { (issue, html), project -> IssueInfoView.IssueInfo(issue, project, html) }
            )
            .doOnSubscribe { viewState.showProgress(true) }
            .doAfterTerminate { viewState.showProgress(false) }
            .subscribe(
                { viewState.showIssue(it) },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
    }
}