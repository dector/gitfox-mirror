package ru.terrakok.gitlabclient.presentation.project.labels

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.entity.Label
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

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        refreshProjectLabels()
    }

    private val paginator = Paginator(
        { page ->
            labelInteractor.getLabelList(projectId, page)
        },
        object : Paginator.ViewController<Label> {
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

            override fun showData(show: Boolean, data: List<Label>) {
                viewState.showLabels(show, data)
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
        paginator.refresh()
    }

    fun loadNextLabelsPage() = paginator.loadNewPage()

    override fun onDestroy() {
        super.onDestroy()
        paginator.release()
    }

}