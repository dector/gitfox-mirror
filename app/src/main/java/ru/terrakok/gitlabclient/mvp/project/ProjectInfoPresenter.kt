package ru.terrakok.gitlabclient.mvp.project

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.extension.addTo
import ru.terrakok.gitlabclient.extension.userMessage
import ru.terrakok.gitlabclient.model.project.ProjectManager
import ru.terrakok.gitlabclient.model.resources.ResourceManager
import timber.log.Timber
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 27.04.17.
 */
@InjectViewState
class ProjectInfoPresenter(private val projectId: Long) : MvpPresenter<ProjectInfoView>() {
    @Inject lateinit var resourceManager: ResourceManager
    @Inject lateinit var router: Router
    @Inject lateinit var projectManager: ProjectManager

    private val compositeDisposable = CompositeDisposable()

    init {
        App.DAGGER.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        projectManager.getProject(projectId)
                .doOnSubscribe { viewState.showProgress(true) }
                .doOnEvent { _, _ -> viewState.showProgress(false) }
                .subscribe(
                        { project -> viewState.showProjectInfo(project) },
                        { error ->
                            Timber.e("getProjects: $error")
                            viewState.showMessage(error.userMessage(resourceManager))
                        }
                )
                .addTo(compositeDisposable)

        viewState.showReadmeFile(projectManager.getProjectReadmePath(projectId))
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    fun onBackPressed() = router.exit()
}