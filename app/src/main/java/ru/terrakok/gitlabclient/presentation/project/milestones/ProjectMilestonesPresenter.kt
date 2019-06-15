package ru.terrakok.gitlabclient.presentation.project.milestones

import com.arellomobile.mvp.InjectViewState
import javax.inject.Inject
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.model.interactor.milestone.MilestoneInteractor
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
    private val flowRouter: FlowRouter
) : BasePresenter<ProjectMilestonesView>() {

    private val projectId = projectIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshMilestones()
    }

    private val paginator = Paginator(
        { milestoneInteractor.getMilestones(projectId, null, it) },
        object : Paginator.ViewController<Milestone> {
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

            override fun showData(show: Boolean, data: List<Milestone>) {
                viewState.showMilestones(show, data)
            }

            override fun showRefreshProgress(show: Boolean) {
                viewState.showRefreshProgress(show)
            }

            override fun showPageProgress(show: Boolean) {
                viewState.showPageProgress(show)
            }
        }
    )

    fun onMilestoneClicked(milestone: Milestone) {
        milestone.webUrl?.let {
            flowRouter.startFlow(Screens.ExternalBrowserFlow(it))
        }
    }

    fun refreshMilestones() = paginator.refresh()
    fun loadNextMilestonesPage() = paginator.loadNewPage()

    override fun onDestroy() {
        super.onDestroy()

        paginator.release()
    }
}