package ru.terrakok.gitlabclient.presentation.files

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.terrakok.gitlabclient.entity.Branch
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.RepositoryTreeNodeType
import ru.terrakok.gitlabclient.entity.app.file.ProjectFile
import ru.terrakok.gitlabclient.entity.app.file.ProjectFileNavigation
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
    private val router: FlowRouter,
    private val projectFileNavigation: ProjectFileNavigation
) : BasePresenter<ProjectFilesView>() {

    private val projectId = projectIdWrapper.value

    private val projectBranches = arrayListOf<Branch>()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        if (projectFileNavigation.isInitiated()) {
            projectInteractor.getProjectBranches(projectId)
                .doOnSubscribe { viewState.showBlockingProgress(true) }
                .doAfterTerminate { viewState.showBlockingProgress(false) }
                .subscribe(
                    {
                        projectBranches.addAll(it)

                        projectFileNavigation.isInRoot().let { inRoot ->
                            viewState.setBranch(if (inRoot) projectFileNavigation.branchName else "")
                            viewState.showBranchSelection(inRoot)
                        }
                        viewState.setPath(projectFileNavigation.getUIPath())

                        refreshFiles()
                    },
                    { errorHandler.proceed(it, { viewState.showMessage(it) }) }
                )
                .connect()
        } else {
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
                        projectFileNavigation.defaultPath = project.path
                        projectFileNavigation.branchName = project.defaultBranch
                        projectFileNavigation.addPath(ProjectFileNavigation.ROOT_PATH)
                        projectBranches.addAll(branches)

                        viewState.setBranch(projectFileNavigation.branchName)
                        viewState.showBranchSelection(true)
                        viewState.setPath(projectFileNavigation.getUIPath())

                        refreshFiles()
                    },
                    { errorHandler.proceed(it, { viewState.showMessage(it) }) }
                )
                .connect()
        }
    }

    private val paginator = Paginator(
        {
            projectInteractor.getProjectFiles(
                projectId,
                projectFileNavigation.getRemotePath(),
                projectFileNavigation.branchName,
                it
            )
        },
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

    fun refreshFiles() = paginator.refresh()
    fun loadNextIssuesPage() = paginator.loadNewPage()
    fun onShowBranchesClick() = viewState.showBranches(projectBranches)

    fun onFileClick(item: ProjectFile) {
        if (item.nodeType == RepositoryTreeNodeType.TREE) {
            if (projectFileNavigation.isInRoot()) {
                viewState.setBranch("")
                viewState.showBranchSelection(false)
            }
            projectFileNavigation.addPath(item.name)
            viewState.setPath(projectFileNavigation.getUIPath())

            refreshFiles()
        } else {
            // TODO: file details (Navigate to file details flow).
        }
    }

    fun onBackPressed() {
        if (!projectFileNavigation.isInRoot()) {
            projectFileNavigation.removeTopPath()
            if (projectFileNavigation.isInRoot()) {
                viewState.setBranch(projectFileNavigation.branchName)
                viewState.showBranchSelection(true)
            }
            viewState.setPath(projectFileNavigation.getUIPath())

            refreshFiles()
        } else {
            router.exit()
        }
    }

    fun onBranchClick(branchName: String) {
        if (projectFileNavigation.branchName != branchName) {
            projectFileNavigation.branchName = branchName
            projectFileNavigation.clearPaths()
            viewState.setBranch(branchName)

            refreshFiles()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        paginator.release()
    }
}