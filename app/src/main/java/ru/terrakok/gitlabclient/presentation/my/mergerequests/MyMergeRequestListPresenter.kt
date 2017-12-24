package ru.terrakok.gitlabclient.presentation.my.mergerequests

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.model.interactor.mergerequest.MergeRequestListInteractor
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import javax.inject.Inject

@InjectViewState
class MyMergeRequestListPresenter @Inject constructor(
        private val mrState: MergeRequestState,
        private val interactor: MergeRequestListInteractor,
        private val errorHandler: ErrorHandler
) : MvpPresenter<MyMergeRequestListView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshMergeRequests()
    }

    private val paginator = Paginator(
            { interactor.getMyMergeRequests(mrState, it) },
            object : Paginator.ViewController<MergeRequest> {
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

                override fun showData(show: Boolean, data: List<MergeRequest>) {
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

    fun onMergeRequestClick(mergeRequest: MergeRequest) {}
    fun refreshMergeRequests() = paginator.refresh()
    fun loadNextMergeRequestsPage() = paginator.loadNewPage()

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
    }
}