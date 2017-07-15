package ru.terrakok.gitlabclient.presentation.my.issues

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.Disposable
import ru.terrakok.gitlabclient.entity.common.Issue
import ru.terrakok.gitlabclient.extension.userMessage
import ru.terrakok.gitlabclient.model.interactor.issue.IssuesInteractor
import ru.terrakok.gitlabclient.model.system.ResourceManager
import timber.log.Timber
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

    private val FIRST_PAGE = 1

    private var currentPage = 0
    private var hasMoreIssues = false
    private var myIssues = mutableListOf<Issue>()

    private var disposable: Disposable? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshIssues()
    }

    fun onIssueClick(issue: Issue) {}
    fun refreshIssues() = loadNewPage(FIRST_PAGE)
    fun loadNextIssuesPage() = loadNewPage(currentPage + 1)

    private fun loadNewPage(page: Int) {
        Timber.d("loadNewPage($page)")
        if (page == FIRST_PAGE) {
            disposable?.dispose()
            disposable = null
            hasMoreIssues = true
        }

        if (disposable == null && hasMoreIssues) {
            disposable = issuesInteractor.getMyIssues(initParams.isOpened, page)
                    .doOnSubscribe {
                        if (page == FIRST_PAGE) viewState.showProgress(true)
                        else viewState.showPageProgress(true)
                    }
                    .doAfterTerminate {
                        if (page == FIRST_PAGE) viewState.showProgress(false)
                        else viewState.showPageProgress(false)

                        disposable?.dispose()
                        disposable = null
                    }
                    .subscribe(
                            { issues ->
                                if (page == FIRST_PAGE) myIssues.clear()
                                myIssues.addAll(issues)
                                viewState.showIssues(myIssues)

                                currentPage = page
                                hasMoreIssues = issues.isNotEmpty()
                            },
                            { error ->
                                Timber.e("getMyIssues($page): $error")
                                viewState.showMessage(error.userMessage(resourceManager))
                            }
                    )
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        disposable?.dispose()
    }
}