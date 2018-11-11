package ru.terrakok.gitlabclient.presentation.project.labels

import com.arellomobile.mvp.InjectViewState
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.gitlabclient.model.interactor.label.LabelInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 11.11.2018.
 */
@InjectViewState
class ProjectLabelsPresenter @Inject constructor(
    @ProjectId projectIdWrapper: PrimitiveWrapper<Long>,
    private val labelInteractor: LabelInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<ProjectLabelsView>() {

    private val projectId = projectIdWrapper.value
    private val toggleSubscriptionsDisposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshProjectLabels()
    }

    private val paginator = Paginator(
        { page ->
            labelInteractor.getLabelList(projectId, page)
                .map {
                    it.map { label -> LabelUi(origin = label, isLoading = false) }
                }
        },
        object : Paginator.ViewController<LabelUi> {
            override fun showEmptyProgress(show: Boolean) {
                viewState.showEmptyProgress(show)
            }

            override fun showEmptyError(show: Boolean, error: Throwable?) {
                if (error != null) {
                    errorHandler.proceed(error) { viewState.showEmptyError(show, it) }
                } else {
                    viewState.showEmptyError(show, null)
                }
            }

            override fun showErrorMessage(error: Throwable) {
                errorHandler.proceed(error) { viewState.showMessage(it) }
            }

            override fun showEmptyView(show: Boolean) {
                viewState.showEmptyView(show)
            }

            override fun showData(show: Boolean, data: List<LabelUi>) {
                viewState.showItems(show, data)
            }

            override fun showRefreshProgress(show: Boolean) {
                viewState.showRefreshProgress(show)
            }

            override fun showPageProgress(show: Boolean) {
                viewState.showPageProgress(show)
            }
        }
    )

    fun refreshProjectLabels() {
        toggleSubscriptionsDisposable.clear()
        paginator.refresh()
    }

    fun loadNextLabelsPage() = paginator.loadNewPage()

    fun toggleSubscription(label: LabelUi) {
        val labelId = label.origin.id
        val toggleAction = if (label.origin.subscribed) {
            labelInteractor.unsubscribeFromLabel(projectId, labelId)
        } else {
            labelInteractor.subscribeToLabel(projectId, labelId)
        }

        toggleAction
            .doOnSubscribe {
                label.copy(isLoading = true)
                    .let { updateLabel(it) }
            }
            .subscribe(
                { updatedLabel ->
                    val uiLabel = LabelUi(origin = updatedLabel, isLoading = false)
                    updateLabel(uiLabel)
                },
                { throwable ->
                    updateLabel(label)
                    errorHandler.proceed(throwable) { viewState.showMessage(it) }
                }
            )
            .also { toggleSubscriptionsDisposable.add(it) }
    }

    private fun updateLabel(label: LabelUi) {
        paginator.updateItem(label) { it.origin.id == label.origin.id }
    }

    override fun onDestroy() {
        super.onDestroy()
        toggleSubscriptionsDisposable.dispose()
        paginator.release()
    }

}