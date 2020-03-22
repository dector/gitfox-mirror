package ru.terrakok.gitlabclient.presentation.projects

import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectListMode
import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.model.interactor.ProjectInteractor
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
        refreshProjects()
        interactor.projectChanges
            .onEach { paginator.proceed(Paginator.Action.Refresh) }
            .launchIn(this)
    }

    private fun loadNewPage(page: Int) {
        pageJob?.cancel()
        pageJob = launch {
            try {
                val data = getProjectsSingle(page)
                paginator.proceed(Paginator.Action.NewPage(page, data))
            } catch (e: Exception) {
                errorHandler.proceed(e)
                paginator.proceed(Paginator.Action.PageError(e))
            }
        }
    }

    private suspend fun getProjectsSingle(page: Int) = when (mode) {
        STARRED_PROJECTS -> interactor.getProjectsList(
            page = page,
            starred = true,
            orderBy = OrderBy.LAST_ACTIVITY_AT,
            archived = false
        )
        MY_PROJECTS -> interactor.getProjectsList(
            page = page,
            owned = true,
            orderBy = OrderBy.LAST_ACTIVITY_AT,
            archived = false
        )
        else -> interactor.getProjectsList(
            page = page,
            membership = true,
            orderBy = OrderBy.LAST_ACTIVITY_AT,
            archived = false
        )
    }

    fun refreshProjects() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextProjectsPage() = paginator.proceed(Paginator.Action.LoadMore)
    fun onProjectClicked(id: Long) = router.startFlow(Screens.ProjectFlow(id))
    fun onBackPressed() = router.exit()
}
