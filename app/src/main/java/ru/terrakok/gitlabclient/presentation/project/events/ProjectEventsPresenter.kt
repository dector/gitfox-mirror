package ru.terrakok.gitlabclient.presentation.project.events

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Observable
import javax.inject.Inject
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.extension.openInfo
import ru.terrakok.gitlabclient.model.interactor.event.EventInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.presentation.global.Paginator

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
@InjectViewState
class ProjectEventsPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    private val eventInteractor: EventInteractor,
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter
) : BasePresenter<ProjectEventsView>() {

    private val projectId = projectIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshEvents()
    }

    private val paginator = Paginator(
        {
            eventInteractor.getProjectEvents(projectId, it)
                .flattenAsObservable { it }
                .concatMap { item ->
                    when (item) {
                        is TargetHeader.Public -> {
                            mdConverter.markdownToSpannable(item.body.toString())
                                .map { md -> item.copy(body = md) }
                                .toObservable()
                        }
                        is TargetHeader.Confidential -> Observable.just(item)
                    }
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

    fun onItemClick(item: TargetHeader.Public) = item.openInfo(router)
    fun onUserClick(userId: Long) = router.startFlow(Screens.UserFlow(userId))
    fun refreshEvents() = paginator.refresh()
    fun loadNextEventsPage() = paginator.loadNewPage()
    fun onBackPressed() = router.exit()

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
    }
}