package ru.terrakok.gitlabclient.presentation.project

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.extension.addTo
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.presentation.global.ErrorHandler
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
        private val errorHandler: ErrorHandler
) : MvpPresenter<ProjectInfoView>() {

    private val compositeDisposable = CompositeDisposable()
    private val projectId = projectIdWrapper.value

    override fun onFirstViewAttach() {
        projectInteractor.getProject(projectId)
                .doOnSuccess { project -> viewState.showProjectInfo(project) }
                .flatMap { project ->
                    projectInteractor.getProjectReadmeHtml(project.id, project.defaultBranch)
                }
                .doOnSubscribe { viewState.showProgress(true) }
                .doAfterTerminate { viewState.showProgress(false) }
                .subscribe(
                        { viewState.showReadmeFile(it) },
                        { errorHandler.proceed(it, { viewState.showMessage(it) }) }
                )
                .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    fun onBackPressed() = router.exit()
}