package ru.terrakok.gitlabclient.presentation.projects

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectListMode
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 30.03.17
 */

@InjectViewState
class ProjectsListPresenter @Inject constructor(
    @ProjectListMode private val modeWrapper: PrimitiveWrapper<Int>,
    private val router: FlowRouter,
    private val interactor: ProjectInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<ProjectsListView>() {

    companion object {
        const val MAIN_PROJECTS = 0
        const val MY_PROJECTS = 1
        const val STARRED_PROJECTS = 2
    }

    private val mode = modeWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshProjects()
    }

    private val paginator = Paginator(
        { getProjectsSingle(it) },
        interactor.projectChanges,
        object : Paginator.ViewController<Project> {
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

            override fun showData(show: Boolean, data: List<Project>) {
                viewState.showProjects(show, data)
            }

            override fun showRefreshProgress(show: Boolean) {
                viewState.showRefreshProgress(show)
            }

            override fun showPageProgress(show: Boolean) {
                viewState.showPageProgress(show)
            }
        }
    )

    private fun getProjectsSingle(page: Int) = when (mode) {
        STARRED_PROJECTS -> interactor.getStarredProjects(page)
        MY_PROJECTS -> interactor.getMyProjects(page)
        else -> interactor.getMainProjects(page)
    }

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
    }

    fun refreshProjects() = paginator.refresh()
    fun loadNextProjectsPage() = paginator.loadNewPage()

    fun onProjectClicked(id: Long) = router.startFlow(Screens.ProjectFlow(id))
    fun onBackPressed() = router.exit()
}