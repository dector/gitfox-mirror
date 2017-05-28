package ru.terrakok.gitlabclient.presentation.project

import com.arellomobile.mvp.MvpPresenter
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.extension.addTo
import ru.terrakok.gitlabclient.extension.userMessage
import ru.terrakok.gitlabclient.model.interactor.project.ProjectInteractor
import ru.terrakok.gitlabclient.model.system.ResourceManager

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 27.04.17.
 */
@com.arellomobile.mvp.InjectViewState
class ProjectInfoPresenter(private val projectId: Long) : MvpPresenter<ProjectInfoView>() {
    @javax.inject.Inject lateinit var resourceManager: ResourceManager
    @javax.inject.Inject lateinit var router: Router
    @javax.inject.Inject lateinit var projectInteractor: ProjectInteractor

    private val compositeDisposable = io.reactivex.disposables.CompositeDisposable()

    init {
        ru.terrakok.gitlabclient.App.Companion.DAGGER.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        projectInteractor.getProject(projectId)
                .doOnSuccess { project -> viewState.showProjectInfo(project) }
                .flatMap { project ->
                    projectInteractor.getProjectReadmeHtml(project.id, project.defaultBranch)
                }
                .doOnSubscribe { viewState.showProgress(true) }
                .doOnEvent { _, _ -> viewState.showProgress(false) }
                .subscribe(
                        { htmlReadme ->
                            viewState.showReadmeFile(htmlReadme)
                        },
                        { error ->
                            timber.log.Timber.e("getProjects: $error")
                            viewState.showMessage(error.userMessage(resourceManager))
                        }
                )
                .addTo(compositeDisposable)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    fun onBackPressed() = router.exit()
}