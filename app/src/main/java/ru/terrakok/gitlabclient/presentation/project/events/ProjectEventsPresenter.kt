package ru.terrakok.gitlabclient.presentation.project.events

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
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
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
@InjectViewState
class ProjectEventsPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    private val eventInteractor: EventInteractor,
    private val mdConverter: MarkDownConverter,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter,
    private val paginator: Paginator.Store<TargetHeader>
) : BasePresenter<ProjectEventsView>() {

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
        refreshEvents()
    }

    private fun loadNewPage(page: Int) {
        pageDisposable?.dispose()
        pageDisposable =
            eventInteractor.getProjectEvents(projectId, page)
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

    fun onItemClick(item: TargetHeader.Public) = item.openInfo(router)
    fun refreshEvents() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextEventsPage() = paginator.proceed(Paginator.Action.LoadMore)
    fun onBackPressed() = router.exit()
}