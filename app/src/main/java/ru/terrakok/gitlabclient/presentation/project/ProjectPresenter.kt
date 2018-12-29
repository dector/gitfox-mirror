package ru.terrakok.gitlabclient.presentation.project

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
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

        projectInteractor
            .getProject(projectId)
            .doOnSubscribe { viewState.showBlockingProgress(true) }
            .doAfterTerminate { viewState.showBlockingProgress(false) }
            .subscribe(
                { viewState.setTitle(it.name, it.webUrl) },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
    }

    fun onBackPressed() = flowRouter.exit()

    fun onLabelPressed() = flowRouter.navigateTo(Screens.ProjectLabels)

    fun onMilestonesClicked() = flowRouter.navigateTo(Screens.ProjectMilestonesContainer)

    fun onFilesPressed() = flowRouter.navigateTo(Screens.ProjectFiles)
}