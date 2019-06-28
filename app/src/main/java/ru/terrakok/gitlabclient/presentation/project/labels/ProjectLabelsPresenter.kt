package ru.terrakok.gitlabclient.presentation.project.labels

import com.arellomobile.mvp.InjectViewState
import io.reactivex.disposables.Disposable
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.entity.Label
import ru.terrakok.gitlabclient.model.interactor.label.LabelInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 11.11.2018.
 */
@InjectViewState
class ProjectLabelsPresenter @Inject constructor(
    @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
    private val labelInteractor: LabelInteractor,
    private val errorHandler: ErrorHandler,
    private val flowRouter: FlowRouter,
    private val paginator: Paginator.Store<Label>
) : BasePresenter<ProjectLabelsView>() {

    private val projectId = projectIdWrapper.value
    private var pageDisposable: Disposable? = null

    init {
        paginator.render = { viewState.renderPaginatorState(it) }
        paginator.sideEffects
            .subscribe { effect ->
                when (effect) {
                    is Paginator.SideEffect.LoadPage -> loadNewPage(effect.currentPage)
                    is Paginator.SideEffect.ErrorEvent -> {
                        errorHandler.proceed(effect.error) { viewState.showMessage(it) }
                    }
                }
            }
            .connect()
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshProjectLabels()
        labelInteractor.labelChanges
            .subscribe { paginator.proceed(Paginator.Action.Refresh) }
            .connect()
    }

    private fun loadNewPage(page: Int) {
        pageDisposable?.dispose()
        pageDisposable =
            labelInteractor.getLabelList(projectId, page)
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

    fun refreshProjectLabels() = paginator.proceed(Paginator.Action.Refresh)
    fun loadNextLabelsPage() = paginator.proceed(Paginator.Action.LoadMore)
    fun onBackPressed() = flowRouter.exit()
}