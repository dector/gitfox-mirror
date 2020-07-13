package ru.terrakok.gitlabclient.presentation.mergerequest.commits

import gitfox.entity.app.CommitWithShortUser
import gitfox.model.interactor.MergeRequestInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.MergeRequestId
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.system.flow.FlowRouter
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
    private var pageJob: Job? = null

    init {
        paginator.render = { viewState.renderPaginatorState(it) }
        launch {
            paginator.sideEffects.consumeEach { effect ->
                when (effect) {
                    is Paginator.SideEffect.LoadPage -> loadNewPage(effect.currentPage)
                    is Paginator.SideEffect.ErrorEvent -> {
                        errorHandler.proceed(effect.error) { viewState.showMessage(it) }
                    }
                }
            }
        }
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshCommits()
    }

    private fun loadNewPage(page: Int) {
        pageJob?.cancel()
        pageJob = launch {
            try {
                val data = mrInteractor.getMergeRequestCommits(projectId, mrId, page)
                paginator.proceed(Paginator.Action.NewPage(page, data))
            } catch (e: Exception) {
                errorHandler.proceed(e)
                paginator.proceed(Paginator.Action.PageError(e))

            }
        }
    }

    fun refreshCommits() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextCommitsPage() = paginator.proceed(Paginator.Action.LoadMore)

    fun onCommitClicked(commitId: String) =
        flowRouter.startFlow(Screens.Commit(commitId, projectId))
}
