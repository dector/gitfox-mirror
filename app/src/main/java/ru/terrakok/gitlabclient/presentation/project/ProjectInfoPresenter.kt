package ru.terrakok.gitlabclient.presentation.project

import com.arellomobile.mvp.InjectViewState
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.presentation.global.BasePresenter
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
import ru.terrakok.gitlabclient.presentation.global.MarkDownConverter
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectId
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 27.04.17.
 */
@InjectViewState
class ProjectInfoPresenter @Inject constructor(
        @ProjectId private val projectIdWrapper: PrimitiveWrapper<Long>,
        private val router: Router,
        private val projectInteractor: ProjectInteractor,
        private val mdConverter: MarkDownConverter,
        private val errorHandler: ErrorHandler
) : BasePresenter<ProjectInfoView>() {

    private val projectId = projectIdWrapper.value

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        projectInteractor.getProject(projectId)
                .doOnSuccess { project -> viewState.showProjectInfo(project) }
                .flatMap { project ->
                    projectInteractor
                            .getProjectReadme(project.id, project.defaultBranch)
                            .flatMap { mdConverter.markdownToSpannable(it) }
                }
                .doOnSubscribe { viewState.showProgress(true) }
                .doAfterTerminate { viewState.showProgress(false) }
                .subscribe(
                        { viewState.showReadmeFile(it) },
                        { errorHandler.proceed(it, { viewState.showMessage(it) }) }
                )
                .connect()
    }

    fun onBackPressed() = router.exit()
}