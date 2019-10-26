package ru.terrakok.gitlabclient.presentation.project.issues

import moxy.InjectViewState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.model.interactor.IssueInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.util.openInfo
import javax.inject.Inject

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 27.08.18
 */
@InjectViewState
class ProjectIssuesPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    private val issueState: IssueState,
    private val issueInteractor: IssueInteractor,
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter,
    private val paginator: Paginator.Store<TargetHeader>
) : BasePresenter<ProjectIssuesView>() {

    private val projectId = projectIdWrapper.value
    private var pageDisposable: Disposable? = null

    init {
        paginator.render = { viewState.renderPaginatorState(it) }
        paginator.sideEffects.subscribe { effect ->
            when (effect) {
                is Paginator.SideEffect.LoadPage -> loadNewPage(effect.currentPage)
                is Paginator.SideEffect.ErrorEvent -> {
                    errorHandler.proceed(effect.error) { viewState.showMessage(it) }
                }
            }
        }.connect()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshIssues()
        issueInteractor.issueChanges
            .subscribe { paginator.proceed(Paginator.Action.Refresh) }
            .connect()
    }

    private fun loadNewPage(page: Int) {
        pageDisposable?.dispose()
        pageDisposable =
            issueInteractor.getIssues(projectId, state = issueState, page = page)
                .flattenAsObservable { it }
                .concatMap { item ->
                    when (item) {
                        is TargetHeader.Public -> {
                            mdConverter.markdownToSpannable(item.body.toString())
                                .map { md -> item.copy(body = md) }
                                .toObservable()
                        }
                        is TargetHeader.Confidential -> Observable.just(item)
                    }
                }
                .toList()
                .subscribe(
                    { data ->
                        paginator.proceed(Paginator.Action.NewPage(page, data))
                    },
                    { e ->
                        errorHandler.proceed(e)
                        paginator.proceed(Paginator.Action.PageError(e))
                    }
                )
        pageDisposable?.connect()
    }

    fun onIssueClick(item: TargetHeader.Public) = item.openInfo(router)
    fun refreshIssues() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextIssuesPage() = paginator.proceed(Paginator.Action.LoadMore)
}