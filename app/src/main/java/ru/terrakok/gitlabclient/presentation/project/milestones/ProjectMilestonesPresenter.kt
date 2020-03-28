package ru.terrakok.gitlabclient.presentation.project.milestones

import gitfox.entity.Milestone
import gitfox.model.interactor.MilestoneInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import javax.inject.Inject

/**
 * @author Valentin Logvinovitch (glvvl) on 24.11.18.
 */
@InjectViewState
class ProjectMilestonesPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    private val milestoneInteractor: MilestoneInteractor,
    private val errorHandler: ErrorHandler,
    private val flowRouter: FlowRouter,
    private val paginator: Paginator.Store<Milestone>
) : BasePresenter<ProjectMilestonesView>() {

    private val projectId = projectIdWrapper.value
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

        refreshMilestones()
        milestoneInteractor.milestoneChanges
            .onEach { paginator.proceed(Paginator.Action.Refresh) }
            .launchIn(this)
    }

    private fun loadNewPage(page: Int) {
        pageJob?.cancel()
        pageJob = launch {
            try {
                val data = milestoneInteractor.getMilestones(projectId, null, page)
                paginator.proceed(Paginator.Action.NewPage(page, data))
            } catch (e: Exception) {
                errorHandler.proceed(e)
                paginator.proceed(Paginator.Action.PageError(e))
            }
        }
    }

    fun onMilestoneClicked(milestone: Milestone) {
        milestone.webUrl?.let {
            flowRouter.startFlow(Screens.ExternalBrowserFlow(it))
        }
    }

    fun refreshMilestones() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextMilestonesPage() = paginator.proceed(Paginator.Action.LoadMore)
}
