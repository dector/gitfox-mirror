package ru.terrakok.gitlabclient.presentation.project.info

import gitfox.model.interactor.ProjectInteractor
import kotlinx.coroutines.launch
import moxy.InjectViewState
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 27.04.17.
 */
@InjectViewState
class ProjectInfoPresenter @Inject constructor(
    @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
    private val projectInteractor: ProjectInteractor,
    private val errorHandler: ErrorHandler
) : BasePresenter<ProjectInfoView>() {

    private val projectId = projectIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        launch {
            viewState.showProgress(true)
            try {
                val project = projectInteractor.getProject(projectId)
                val readme = runCatching {
                    projectInteractor.getProjectReadme(project)
                }.getOrDefault("")
                viewState.showProject(project, readme)
            } catch (e: Exception) {
                errorHandler.proceed(e) { viewState.showMessage(it) }
            }
            viewState.showProgress(false)
        }
    }
}
