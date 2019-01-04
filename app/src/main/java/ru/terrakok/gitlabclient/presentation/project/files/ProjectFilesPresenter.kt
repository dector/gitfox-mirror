package ru.terrakok.gitlabclient.presentation.project.files

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Branch
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.RepositoryTreeNodeType
import ru.terrakok.gitlabclient.entity.app.ProjectFile
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.model.system.ResourceManager
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
    private val projectFileDestination: ProjectFileDestination,
    private val resourceManager: ResourceManager
) : BasePresenter<ProjectFilesView>() {

    private val projectId = projectIdWrapper.value
    private val projectBranches = arrayListOf<Branch>()

    init {
        projectFileDestination.setCallback(object : ProjectFileDestination.Callback {
            override fun onMoveForward(fromRoot: Boolean) {
                val inRoot = projectFileDestination.isInRoot()
                if (fromRoot) {
                    viewState.setBranch("")
                    viewState.showBranchSelection(false)
                } else if (inRoot) {
                    viewState.setBranch(projectFileDestination.branchName)
                    viewState.showBranchSelection(true)
                }
                viewState.setPath(getUIPath(inRoot, projectFileDestination.defaultPath, projectFileDestination.paths))

                refreshFiles()
            }

            override fun onMoveBack(fromRoot: Boolean) {
                if (fromRoot) {
                    router.exit()
                } else {
                    val inRoot = projectFileDestination.isInRoot()
                    if (inRoot) {
                        viewState.setBranch(projectFileDestination.branchName)
                        viewState.showBranchSelection(true)
                    }
                    viewState.setPath(
                        getUIPath(inRoot, projectFileDestination.defaultPath, projectFileDestination.paths)
                    )

                    refreshFiles()
                }
            }

            override fun onBranchChange(branchName: String) {
                viewState.setBranch(branchName)

                refreshFiles()
            }
        })
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        if (projectFileDestination.isInitiated()) {
            loadBranches()
        } else {
            loadProjectWithBranches()
        }
    }

    private fun loadBranches() {
        projectInteractor.getProjectBranches(projectId)
            .doOnSubscribe { viewState.showBlockingProgress(true) }
            .doAfterTerminate { viewState.showBlockingProgress(false) }
            .subscribe(
                {
                    viewState.showBranchSelection(true)

                    projectBranches.addAll(it)
                    projectFileDestination.moveToRoot()
                },
                { handleLoadingProjectDetailsError(it) }
            )
            .connect()
    }

    private fun loadProjectWithBranches() {
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
                    if (project.defaultBranch != null) {
                        viewState.showBranchSelection(true)

                        projectBranches.addAll(branches)
                        projectFileDestination.init(project.path, project.defaultBranch)
                        projectFileDestination.moveToRoot()
                    } else {
                        handleLoadingProjectDetailsError(NoBranchesError())
                    }
                },
                { handleLoadingProjectDetailsError(it) }
            )
            .connect()
    }

    private fun handleLoadingProjectDetailsError(error: Throwable) {
        viewState.setPath(resourceManager.getString(R.string.project_files_default_path))
        viewState.showBranchSelection(false)
        errorHandler.proceed(error, { viewState.showEmptyError(true, it) })
    }

    private val paginator = Paginator(
        {
            projectInteractor.getProjectFiles(
                projectId,
                getRemotePath(projectFileDestination.isInRoot(), projectFileDestination.paths),
                projectFileDestination.branchName,
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
                // Hide old files to prevent navigation into the same directories.
                viewState.showEmptyView(true)
                viewState.showFiles(false, emptyList())
                errorHandler.proceed(error, { viewState.showEmptyError(true, it) })
            }

            override fun showEmptyView(show: Boolean) {
                viewState.showEmptyView(show)
            }

            override fun showData(show: Boolean, data: List<ProjectFile>) {
                // Hide empty view, if it was hidden in case of error.
                viewState.showEmptyView(false)
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

    fun refreshFiles() {
        if (projectFileDestination.isInitiated()) {
            paginator.refresh()
        } else {
            viewState.showEmptyError(false, null)
            viewState.showRefreshProgress(false)

            loadProjectWithBranches()
        }
    }

    fun loadNextFilesPage() = paginator.loadNewPage()
    fun onShowBranchesClick() = viewState.showBranches(projectBranches)
    fun onBackPressed() = projectFileDestination.moveBack()
    fun onBranchClick(branchName: String) = projectFileDestination.changeBranch(branchName)

    fun onFileClick(item: ProjectFile) {
        if (item.nodeType == RepositoryTreeNodeType.TREE) {
            projectFileDestination.moveForward(item.name)
        } else {
            // TODO: file details (Navigate to file details flow).
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        paginator.release()
    }

    companion object {
        private const val UI_SEPARATOR = " / "
        private const val REMOTE_SEPARATOR = "/"

        private fun getUIPath(inRoot: Boolean, defaultPath: String, paths: List<String>) =
            if (inRoot) {
                defaultPath
            } else {
                "$defaultPath$UI_SEPARATOR${paths.subList(1, paths.size).joinToString(separator = UI_SEPARATOR)}"
            }

        private fun getRemotePath(inRoot: Boolean, paths: List<String>) =
            if (inRoot) {
                ""
            } else {
                paths.subList(1, paths.size).joinToString(separator = REMOTE_SEPARATOR)
            }
    }
}