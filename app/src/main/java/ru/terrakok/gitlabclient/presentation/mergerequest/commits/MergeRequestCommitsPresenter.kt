package ru.terrakok.gitlabclient.presentation.mergerequest.commits

import moxy.InjectViewState
import io.reactivex.disposables.Disposable
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.MergeRequestId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.app.CommitWithShortUser
import ru.terrakok.gitlabclient.model.interactor.MergeRequestInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 20.10.18.
 */
@InjectViewState
class MergeRequestCommitsPresenter @Inject constructor(
    @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
    @MergeRequestId mrIdWrapper: PrimitiveWrapper<Long>,
    private val mrInteractor: MergeRequestInteractor,
    private val errorHandler: ErrorHandler,
    private val paginator: Paginator.Store<CommitWithShortUser>,
    private val flowRouter: FlowRouter
) : BasePresenter<MergeRequestCommitsView>() {

    private val projectId = projectIdWrapper.value
    private val mrId = mrIdWrapper.value
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

        refreshCommits()
    }

    private fun loadNewPage(page: Int) {
        pageDisposable?.dispose()
        pageDisposable =
            mrInteractor.getMergeRequestCommits(projectId, mrId, page)
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

    fun refreshCommits() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextCommitsPage() = paginator.proceed(Paginator.Action.LoadMore)

    fun onCommitClicked(commitId: String) =
        flowRouter.startFlow(Screens.Commit(commitId, projectId))
}