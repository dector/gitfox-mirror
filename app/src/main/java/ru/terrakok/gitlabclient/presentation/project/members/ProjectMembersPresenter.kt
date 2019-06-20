package ru.terrakok.gitlabclient.presentation.project.members

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.Member
import ru.terrakok.gitlabclient.model.interactor.members.MembersInteractor
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
    private val router: FlowRouter
) : BasePresenter<ProjectMembersView>() {

    private val projectId = projectIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshMembers()
    }

    private val paginator = Paginator(
        { membersInteractor.getMembers(projectId, it) },
        membersInteractor.memberChanges,
        object : Paginator.ViewController<Member> {
            override fun showEmptyProgress(show: Boolean) {
                viewState.showEmptyProgress(show)
            }

            override fun showEmptyError(show: Boolean, error: Throwable?) {
                if (error != null) {
                    errorHandler.proceed(error) { viewState.showEmptyError(show, it) }
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

            override fun showData(show: Boolean, data: List<Member>) {
                viewState.showMembers(show, data)
            }

            override fun showRefreshProgress(show: Boolean) {
                viewState.showRefreshProgress(show)
            }

            override fun showPageProgress(show: Boolean) {
                viewState.showPageProgress(show)
            }
        }
    )

    fun onMemberClick(userId: Long) {
        //TODO Member Flow(refactor this logic when Member Flow was be ready).
        router.startFlow(Screens.UserFlow(userId))
    }

    fun refreshMembers() = paginator.refresh()
    fun loadNextMembersPage() = paginator.loadNewPage()
    fun onBackPressed() = router.exit()

    override fun onDestroy() {
        super.onDestroy()

        paginator.release()
    }
}