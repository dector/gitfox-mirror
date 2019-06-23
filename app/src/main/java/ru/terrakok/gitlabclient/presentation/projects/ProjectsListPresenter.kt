package ru.terrakok.gitlabclient.presentation.projects

import com.arellomobile.mvp.InjectViewState
import io.reactivex.disposables.Disposable
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
    private val errorHandler: ErrorHandler,
    private val paginator: Paginator.Store<Project>
) : BasePresenter<ProjectsListView>() {

    companion object {
        const val MAIN_PROJECTS = 0
        const val MY_PROJECTS = 1
        const val STARRED_PROJECTS = 2
    }

    private val mode = modeWrapper.value
    private var pageDisposable: Disposable? = null

    init {
        paginator.render = { viewState.renderPaginatorState(it) }
        paginator.sideEffects.subscribe { effect ->
            when (effect) {
                is Paginator.SideEffect.LoadPage -> loadNewPage(effect.currentPage)
            }
        }.connect()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshProjects()
        interactor.projectChanges
            .subscribe { paginator.proceed(Paginator.Action.Refresh) }
            .connect()
    }

    private fun loadNewPage(page: Int) {
        pageDisposable?.dispose()
        pageDisposable =
            getProjectsSingle(page)
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

    private fun getProjectsSingle(page: Int) = when (mode) {
        STARRED_PROJECTS -> interactor.getStarredProjects(page)
        MY_PROJECTS -> interactor.getMyProjects(page)
        else -> interactor.getMainProjects(page)
    }

    fun refreshProjects() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextProjectsPage() = paginator.proceed(Paginator.Action.LoadMore)
    fun onProjectClicked(id: Long) = router.startFlow(Screens.ProjectFlow(id))
    fun onBackPressed() = router.exit()
}