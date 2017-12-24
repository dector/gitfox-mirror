package ru.terrakok.gitlabclient.presentation.projects

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.model.interactor.projects.MainProjectsListInteractor
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectListMode
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 30.03.17
 */

@InjectViewState
class ProjectsListPresenter @Inject constructor(
        @ProjectListMode private val modeWrapper: PrimitiveWrapper<Int>,
        private val router: Router,
        private val mainProjectsListInteractor: MainProjectsListInteractor,
        private val errorHandler: ErrorHandler
) : MvpPresenter<ProjectsListView>() {

    companion object {
        const val MAIN_PROJECTS = 0
        const val MY_PROJECTS = 1
        const val STARRED_PROJECTS = 2
    }

    private val mode = modeWrapper.value

    override fun onFirstViewAttach() {
        refreshProjects()
    }

    private val paginator = Paginator(
            { getProjectsSingle(it) },
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
        STARRED_PROJECTS -> mainProjectsListInteractor.getStarredProjects(page)
        MY_PROJECTS -> mainProjectsListInteractor.getMyProjects(page)
        else -> mainProjectsListInteractor.getMainProjects(page)
    }

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
    }

    fun refreshProjects() = paginator.refresh()
    fun loadNextProjectsPage() = paginator.loadNewPage()

    fun onProjectClicked(id: Long) = router.navigateTo(Screens.PROJECT_INFO_SCREEN, id)
    fun onBackPressed() = router.exit()
}