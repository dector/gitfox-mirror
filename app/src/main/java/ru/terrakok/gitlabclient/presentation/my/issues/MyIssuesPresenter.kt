package ru.terrakok.gitlabclient.presentation.my.issues

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.extension.openInfo
import ru.terrakok.gitlabclient.model.interactor.issue.IssueInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
@InjectViewState
class MyIssuesPresenter @Inject constructor(
    initFilter: Filter,
    private val issueInteractor: IssueInteractor,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter
) : BasePresenter<MyIssuesView>() {
    data class Filter(val createdByMe: Boolean, val onlyOpened: Boolean)

    private var filter = initFilter

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshIssues()
    }

    private val paginator = Paginator(
        { issueInteractor.getMyIssues(filter.createdByMe, filter.onlyOpened, it) },
        object : Paginator.ViewController<TargetHeader> {
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

            override fun showData(show: Boolean, data: List<TargetHeader>) {
                viewState.showIssues(show, data)
            }

            override fun showRefreshProgress(show: Boolean) {
                viewState.showRefreshProgress(show)
            }

            override fun showPageProgress(show: Boolean) {
                viewState.showPageProgress(show)
            }
        }
    )

    fun applyNewFilter(filter: Filter) {
        if (this.filter != filter) {
            this.filter = filter
            paginator.restart()
        }
    }

    fun onIssueClick(item: TargetHeader.Public) = item.openInfo(router)
    fun onUserClick(userId: Long) = router.startFlow(Screens.UserFlow(userId))
    fun refreshIssues() = paginator.refresh()
    fun loadNextIssuesPage() = paginator.loadNewPage()

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
    }
}