package ru.terrakok.gitlabclient.presentation.my.mergerequests

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.extension.openInfo
import ru.terrakok.gitlabclient.model.interactor.mergerequest.MergeRequestInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.presentation.global.ProjectMarkDownConverterProvider
import javax.inject.Inject

@InjectViewState
class MyMergeRequestsPresenter @Inject constructor(
    initFilter: Filter,
    private val interactor: MergeRequestInteractor,
    private val mdConverterProvider: ProjectMarkDownConverterProvider,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter
) : BasePresenter<MyMergeRequestListView>() {
    data class Filter(val createdByMe: Boolean, val onlyOpened: Boolean)

    private var filter = initFilter

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshMergeRequests()
    }

    private val paginator = Paginator(
        {
            interactor.getMyMergeRequests(filter.createdByMe, filter.onlyOpened, it)
                .flattenAsObservable { it }
                .concatMap { item ->
                    mdConverterProvider
                        .getMarkdownConverter(item.projectId)
                        .flatMap { converter ->
                            converter
                                .markdownToSpannable(item.body.toString())
                                .map { md -> item.copy(body = md) }
                        }
                        .toObservable()
                }
                .toList()
        },
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
                viewState.showMergeRequests(show, data)
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

    fun onMergeRequestClick(item: TargetHeader) = item.openInfo(router)
    fun onUserClick(userId: Long) = router.startFlow(Screens.USER_FLOW, userId)
    fun refreshMergeRequests() = paginator.refresh()
    fun loadNextMergeRequestsPage() = paginator.loadNewPage()

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
    }
}