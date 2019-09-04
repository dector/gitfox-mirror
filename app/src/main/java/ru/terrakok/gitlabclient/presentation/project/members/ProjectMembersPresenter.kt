package ru.terrakok.gitlabclient.presentation.project.members

import com.arellomobile.mvp.InjectViewState
import io.reactivex.disposables.Disposable
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.Member
import ru.terrakok.gitlabclient.model.interactor.MembersInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import javax.inject.Inject

/**
 * @author Valentin Logvinovitch (glvvl) on 28.02.19.
 */
@InjectViewState
class ProjectMembersPresenter @Inject constructor(
    @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
    private val membersInteractor: MembersInteractor,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter,
    private val paginator: Paginator.Store<Member>
) : BasePresenter<ProjectMembersView>() {

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

        refreshMembers()
        membersInteractor.memberChanges
            .subscribe { paginator.proceed(Paginator.Action.Refresh) }
            .connect()
    }

    private fun loadNewPage(page: Int) {
        pageDisposable?.dispose()
        pageDisposable =
            membersInteractor.getMembers(projectId, page)
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

    fun onMemberClick(userId: Long) {
        // TODO Member Flow(refactor this logic when Member Flow was be ready).
        router.startFlow(Screens.UserFlow(userId))
    }

    fun refreshMembers() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextMembersPage() = paginator.proceed(Paginator.Action.LoadMore)
    fun onBackPressed() = router.exit()
}