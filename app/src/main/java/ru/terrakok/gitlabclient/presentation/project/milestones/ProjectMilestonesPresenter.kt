package ru.terrakok.gitlabclient.presentation.project.milestones

import io.reactivex.disposables.Disposable
import javax.inject.Inject
import moxy.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.model.interactor.MilestoneInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator

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

        refreshMilestones()
        milestoneInteractor.milestoneChanges
            .subscribe { paginator.proceed(Paginator.Action.Refresh) }
            .connect()
    }

    private fun loadNewPage(page: Int) {
        pageDisposable?.dispose()
        pageDisposable =
            milestoneInteractor.getMilestones(projectId, null, page)
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

    fun onMilestoneClicked(milestone: Milestone) {
        milestone.webUrl?.let {
            flowRouter.startFlow(Screens.ExternalBrowserFlow(it))
        }
    }

    fun refreshMilestones() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextMilestonesPage() = paginator.proceed(Paginator.Action.LoadMore)
}
