package ru.terrakok.gitlabclient.presentation.files

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.terrakok.gitlabclient.entity.Branch
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.app.ProjectFile
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
class ProjectFilesPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    private val projectInteractor: ProjectInteractor,
    private val errorHandler: ErrorHandler,
    private val router: FlowRouter
) : BasePresenter<ProjectFilesView>() {

    private val projectId = projectIdWrapper.value

    private val projectBranches = arrayListOf<Branch>()
    private lateinit var currentBranchName: String

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        Single
            .zip(
                projectInteractor.getProject(projectId),
                projectInteractor.getProjectBranches(projectId),
                BiFunction<Project, List<Branch>, Pair<Project, List<Branch>>> { project, branches ->
                    Pair(project, branches)
                }
            )
            .doOnSubscribe { viewState.showBlockingProgress(true) }
            .doAfterTerminate { viewState.showBlockingProgress(false) }
            .subscribe(
                { (project, branches) ->
                    currentBranchName = project.defaultBranch
                    projectBranches.addAll(branches)

                    // TODO: project files (Implement sub-directories navigation).
                    viewState.setPath("Files")
                    viewState.setBranch(currentBranchName)

                    refreshFiles()
                },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
    }

    private val paginator = Paginator(
        { projectInteractor.getProjectFiles(projectId, "", currentBranchName, it) },
        object : Paginator.ViewController<ProjectFile> {
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

            override fun showData(show: Boolean, data: List<ProjectFile>) {
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

    fun onFileClick(item: ProjectFile) {}
    fun refreshFiles() = paginator.refresh()
    fun loadNextIssuesPage() = paginator.loadNewPage()
    fun onBackPressed() = router.exit()
    fun onShowBranchesClick() = viewState.showBranches(projectBranches)
    fun onBranchClick(branchName: String) {
        if (currentBranchName != branchName) {
            currentBranchName = branchName
            viewState.setBranch(branchName)
            paginator.refresh()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        paginator.release()
    }
}