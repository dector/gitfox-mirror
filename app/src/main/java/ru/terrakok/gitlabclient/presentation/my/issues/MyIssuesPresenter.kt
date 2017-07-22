package ru.terrakok.gitlabclient.presentation.my.issues

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.gitlabclient.entity.Issue
import ru.terrakok.gitlabclient.extension.userMessage
import ru.terrakok.gitlabclient.model.interactor.issue.IssuesInteractor
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.presentation.global.Paginator
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
@InjectViewState
class MyIssuesPresenter @Inject constructor(
        private val initParams: InitParams,
        private val issuesInteractor: IssuesInteractor,
        private val resourceManager: ResourceManager
) : MvpPresenter<MyIssuesView>() {
    data class InitParams(val isOpened: Boolean)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshIssues()
    }

    private val paginator = Paginator(
            { issuesInteractor.getMyIssues(initParams.isOpened, it) },
            object : Paginator.ViewController<Issue> {
                override fun showEmptyProgress(show: Boolean) {
                    viewState.showEmptyProgress(show)
                }

                override fun showEmptyError(show: Boolean, error: Throwable?) {
                    viewState.showEmptyError(show, error?.userMessage(resourceManager))
                }

                override fun showErrorMessage(error: Throwable) {
                    viewState.showMessage(error.userMessage(resourceManager))
                }

                override fun showEmptyView(show: Boolean) {
                    viewState.showEmptyView(show)
                }

                override fun showData(show: Boolean, data: List<Issue>) {
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

    fun onIssueClick(issue: Issue) {}
    fun refreshIssues() = paginator.refresh()
    fun loadNextIssuesPage() = paginator.loadNewPage()

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
    }
}