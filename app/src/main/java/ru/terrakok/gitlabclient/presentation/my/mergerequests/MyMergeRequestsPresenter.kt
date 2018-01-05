package ru.terrakok.gitlabclient.presentation.my.mergerequests

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.extension.openInfo
import ru.terrakok.gitlabclient.model.interactor.mergerequest.MergeRequestInteractor
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import javax.inject.Inject

@InjectViewState
class MyMergeRequestsPresenter @Inject constructor(
        private val initParams: InitParams,
        private val interactor: MergeRequestInteractor,
        private val errorHandler: ErrorHandler,
        private val router: Router
) : MvpPresenter<MyMergeRequestListView>() {
    data class InitParams(val createdByMe: Boolean)

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshMergeRequests()
    }

    private val paginator = Paginator(
            { interactor.getMyMergeRequests(initParams.createdByMe, it) },
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

    fun onMergeRequestClick(item: TargetHeader) = item.openInfo(router)
    fun onUserClick(userId: Long) = router.navigateTo(Screens.USER_INFO_SCREEN, userId)
    fun refreshMergeRequests() = paginator.refresh()
    fun loadNextMergeRequestsPage() = paginator.loadNewPage()

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
    }
}