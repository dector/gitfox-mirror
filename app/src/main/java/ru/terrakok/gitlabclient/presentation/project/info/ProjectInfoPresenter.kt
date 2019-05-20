package ru.terrakok.gitlabclient.presentation.project.info

import com.arellomobile.mvp.InjectViewState
import io.reactivex.Single
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectId
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
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

        projectInteractor
            .getProject(projectId)
            .flatMap { project ->
                projectInteractor
                    .getProjectReadme(project)
                    .onErrorResumeNext { throwable ->
                        when (throwable) {
                            is ProjectInteractor.ReadmeNotFound -> Single.just("")
                            else -> Single.error(throwable)
                        }
                    }
                    .map { mdReadme -> Pair(project, mdReadme) }
            }
            .doOnSubscribe { viewState.showProgress(true) }
            .doAfterTerminate { viewState.showProgress(false) }
            .subscribe(
                { (project, mdReadme) -> viewState.showProject(project, mdReadme) },
                { errorHandler.proceed(it, { viewState.showMessage(it) }) }
            )
            .connect()
    }
}