package ru.terrakok.gitlabclient.presentation.project

import gitfox.model.interactor.ProjectInteractor
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.system.flow.FlowRouter
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 01.11.18.
 */
@InjectViewState
class ProjectPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    private val projectInteractor: ProjectInteractor,
    private val errorHandler: ErrorHandler,
    private val flowRouter: FlowRouter
) : BasePresenter<ProjectView>() {

    private val projectId = projectIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        launch {
            viewState.showBlockingProgress(true)
            try {
                val project = projectInteractor.getProject(projectId)
                viewState.setTitle(project.name, project.webUrl)
            } catch (e: Exception) {
                errorHandler.proceed(e) { viewState.showMessage(it) }
            }
            viewState.showBlockingProgress(false)
        }
    }

    fun onBackPressed() = flowRouter.exit()

    fun onFilesPressed() = flowRouter.navigateTo(Screens.ProjectFiles)
}
