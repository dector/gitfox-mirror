package ru.terrakok.gitlabclient.presentation.project.files

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
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

    private var pageDisposable: Disposable? = null
    private val paginator = Paginator.Store<ProjectFile>().apply {
        render = { viewState.renderPaginatorState(it) }
        sideEffectListener = { effect ->
            when (effect) {
                is Paginator.SideEffect.LoadPage -> loadNewPage(effect.currentPage)
            }
        }
    }

    init {
        projectFileDestination.setCallback(object : ProjectFileDestination.Callback {
            override fun onMoveForward(fromRoot: Boolean) {
                with(projectFileDestination) {
                    if (fromRoot) {
                        viewState.setBranch(branchName)
                    }
                    viewState.setPath(getUIPath(isInRoot(), defaultPath, paths))
                }

                refreshFiles()
            }

            override fun onMoveBack(fromRoot: Boolean) {
                if (fromRoot) {
                    router.exit()
                } else {
                    with(projectFileDestination) {
                        viewState.setPath(getUIPath(isInRoot(), defaultPath, paths))
                    }

                    refreshFiles()
                }
            }

            override fun onBranchChange(branchName: String) {
                viewState.setBranch(branchName)
                with(projectFileDestination) {
                    viewState.setPath(getUIPath(isInRoot(), defaultPath, paths))
                }

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

    private fun loadNewPage(currentPage: Int) {
        pageDisposable?.dispose()
        pageDisposable =
            projectInteractor
                .getProjectFiles(
                    projectId,
                    getRemotePath(projectFileDestination.isInRoot(), projectFileDestination.paths),
                    projectFileDestination.branchName,
                    currentPage + 1
                )
                .subscribe(
                    { data ->
                        paginator.proceed(Paginator.Action.NewPage(data))
                    },
                    { e ->
                        errorHandler.proceed(e)
                        paginator.proceed(Paginator.Action.PageError(e))
                    }
                )
        pageDisposable?.connect()
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
        if (projectFileDestination.isInitiated()) {
            with(projectFileDestination) {
                viewState.setBranch(branchName)
                viewState.setPath(getUIPath(isInRoot(), defaultPath, paths))
            }
            viewState.showBranchSelection(true)
        } else {
            viewState.setPath(resourceManager.getString(R.string.project_files_default_path))
            viewState.showBranchSelection(false)
        }
        if (error is NoBranchesError) {
            viewState.showMessage(resourceManager.getString(R.string.project_files_no_branches))
        } else {
            errorHandler.proceed(error) { viewState.showMessage(it) }
        }
    }

    fun refreshFiles() {
        if (projectFileDestination.isInitiated()) {
            paginator.proceed(Paginator.Action.Refresh)
        } else {
            loadProjectWithBranches()
        }
    }

    fun loadNextFilesPage() = paginator.proceed(Paginator.Action.LoadMore)
    fun onShowBranchesClick() = viewState.showBranches(projectBranches)
    fun onBackPressed() = projectFileDestination.moveBack()
    fun onNavigationCloseClicked() = router.exit()
    fun onBranchClick(branchName: String) = projectFileDestination.changeBranch(branchName)

    fun onFileClick(item: ProjectFile) {
        if (item.nodeType == RepositoryTreeNodeType.TREE) {
            projectFileDestination.moveForward(item.name)
        } else {
            router.startFlow(
                Screens.ProjectFile(
                    projectId,
                    getFilePath(projectFileDestination.isInRoot(), projectFileDestination.paths, item.name),
                    projectFileDestination.branchName
                )
            )
        }
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

        private fun getFilePath(inRoot: Boolean, paths: List<String>, fileName: String) =
            if (inRoot) {
                fileName
            } else {
                "${paths.subList(1, paths.size).joinToString(separator = REMOTE_SEPARATOR)}$REMOTE_SEPARATOR$fileName"
            }
    }
}