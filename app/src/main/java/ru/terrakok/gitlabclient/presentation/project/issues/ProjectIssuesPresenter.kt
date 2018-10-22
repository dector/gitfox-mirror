package ru.terrakok.gitlabclient.presentation.project.issues

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.extension.openInfo
import ru.terrakok.gitlabclient.model.interactor.issue.IssueInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.global.ProjectMarkDownConverterProvider
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import javax.inject.Inject

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 27.08.18
 */
@InjectViewState
class ProjectIssuesPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    private val issueState: IssueState,
    private val issueInteractor: IssueInteractor,
    private val mdConverterProvider: ProjectMarkDownConverterProvider,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter
) : BasePresenter<ProjectIssuesView>() {

    private val projectId = projectIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshIssues()
    }

    private val paginator = Paginator(
        {
            issueInteractor.getIssues(projectId, issueState, it)
                .flattenAsObservable { it }
                .concatMap { item ->
                    mdConverterProvider
                        .getMarkdownConverter(projectId)
                        .flatMap { converter ->
                            converter
                                .markdownToSpannable(item.body.toString())
                                .map { md -> item.copy(body = md) }
                        }
                        .toObservable()
                }
                .toList()
        },
        object : Paginator.ViewController<TargetHeader> {
            override fun showEmptyProgress(show: Boolean) {
                viewState.showEmptyProgress(show)
            }

            override fun showEmptyError(show: Boolean, error: Throwable?) {
                if (error != null) {
                    errorHandler.proceed(error, { viewState.showEmptyError(show, it) })
                } else {
                    viewState.showEmptyError(show, null)
                }
            }

            override fun showErrorMessage(error: Throwable) {
                errorHandler.proceed(error, { viewState.showMessage(it) })
            }

            override fun showEmptyView(show: Boolean) {
                viewState.showEmptyView(show)
            }

            override fun showData(show: Boolean, data: List<TargetHeader>) {
                viewState.showIssues(show, data)
            }

            override fun showRefreshProgress(show: Boolean) {
                viewState.showRefreshProgress(show)
            }

            override fun showPageProgress(show: Boolean) {
                viewState.showPageProgress(show)
            }
        }
    )

    fun onIssueClick(item: TargetHeader) = item.openInfo(router)
    fun onUserClick(userId: Long) = router.startFlow(Screens.USER_FLOW, userId)
    fun refreshIssues() = paginator.refresh()
    fun loadNextIssuesPage() = paginator.loadNewPage()

    override fun onDestroy() {
        super.onDestroy()

        paginator.release()
    }
}