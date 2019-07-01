package ru.terrakok.gitlabclient.presentation.my.mergerequests

import com.arellomobile.mvp.InjectViewState
import io.reactivex.disposables.Disposable
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.extension.openInfo
import ru.terrakok.gitlabclient.model.interactor.mergerequest.MergeRequestInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import javax.inject.Inject

@InjectViewState
class MyMergeRequestsPresenter @Inject constructor(
    initFilter: Filter,
    private val interactor: MergeRequestInteractor,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter,
    private val paginator: Paginator.Store<TargetHeader>
) : BasePresenter<MyMergeRequestListView>() {
    data class Filter(val createdByMe: Boolean, val onlyOpened: Boolean)

    private var filter = initFilter
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

        refreshMergeRequests()
        interactor.mergeRequestChanges
            .subscribe { paginator.proceed(Paginator.Action.Refresh) }
            .connect()
    }

    private fun loadNewPage(page: Int) {
        pageDisposable?.dispose()
        pageDisposable =
            interactor.getMyMergeRequests(filter.createdByMe, filter.onlyOpened, page)
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

    fun applyNewFilter(filter: Filter) {
        if (this.filter != filter) {
            this.filter = filter
            paginator.proceed(Paginator.Action.Restart)
        }
    }

    fun onMergeRequestClick(item: TargetHeader.Public) = item.openInfo(router)
    fun refreshMergeRequests() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextMergeRequestsPage() = paginator.proceed(Paginator.Action.LoadMore)
}