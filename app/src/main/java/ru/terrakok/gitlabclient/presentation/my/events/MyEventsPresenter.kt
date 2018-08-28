package ru.terrakok.gitlabclient.presentation.my.events

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.extension.openInfo
import ru.terrakok.gitlabclient.model.interactor.event.EventInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.presentation.global.Paginator
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
@InjectViewState
class MyEventsPresenter @Inject constructor(
        private val eventInteractor: EventInteractor,
        private val mdConverter: MarkDownConverter,
        private val menuController: GlobalMenuController,
        private val errorHandler: ErrorHandler,
        private val router: FlowRouter
) : BasePresenter<MyEventsView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshEvents()
    }

    private val paginator = Paginator(
            {
                eventInteractor.getEvents(it)
                        .flattenAsObservable { it }
                        .concatMap { item ->
                            mdConverter.markdownToSpannable(item.body.toString())
                                    .map { md -> item.copy(body = md) }
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
                    viewState.showEvents(show, data)
                }

                override fun showRefreshProgress(show: Boolean) {
                    viewState.showRefreshProgress(show)
                }

                override fun showPageProgress(show: Boolean) {
                    viewState.showPageProgress(show)
                }
            }
    )

    fun onMenuClick() = menuController.open()
    fun onItemClick(item: TargetHeader) = item.openInfo(router)
    fun onUserClick(userId: Long) = router.startFlow(Screens.USER_FLOW, userId)
    fun refreshEvents() = paginator.refresh()
    fun loadNextEventsPage() = paginator.loadNewPage()

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
    }
}