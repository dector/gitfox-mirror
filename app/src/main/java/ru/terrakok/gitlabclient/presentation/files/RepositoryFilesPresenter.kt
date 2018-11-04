package ru.terrakok.gitlabclient.presentation.files

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.entity.app.RepositoryFile
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.Paginator
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 02.11.18.
 */
@InjectViewState
class RepositoryFilesPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    private val projectInteractor: ProjectInteractor,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter
) : BasePresenter<RepositoryFilesView>() {

    private val projectId = projectIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        refreshFiles()
    }

    private val paginator = Paginator(
        { projectInteractor.getRepositoryFiles(projectId, "", "develop", it) },
        object : Paginator.ViewController<RepositoryFile> {
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

            override fun showData(show: Boolean, data: List<RepositoryFile>) {
                viewState.showFiles(show, data)
            }

            override fun showRefreshProgress(show: Boolean) {
                viewState.showRefreshProgress(show)
            }

            override fun showPageProgress(show: Boolean) {
                viewState.showPageProgress(show)
            }
        }
    )

    fun onFileClick(item: RepositoryFile) {}
    fun refreshFiles() = paginator.refresh()
    fun loadNextIssuesPage() = paginator.loadNewPage()
    fun onBackPressed() = router.finishFlow()

    override fun onDestroy() {
        super.onDestroy()

        paginator.release()
    }
}