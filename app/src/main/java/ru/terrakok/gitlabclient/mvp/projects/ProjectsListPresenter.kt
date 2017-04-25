package ru.terrakok.gitlabclient.mvp.projects

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.Disposable
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.extension.userMessage
import ru.terrakok.gitlabclient.model.project.MainProjectsListManager
import ru.terrakok.gitlabclient.model.resources.ResourceManager
import timber.log.Timber
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 30.03.17
 */

@InjectViewState
class ProjectsListPresenter(private val mode: Int) : MvpPresenter<ProjectsListView>() {
    companion object {
        const val MAIN_PROJECTS = 0
        const val MY_PROJECTS = 1
        const val STARRED_PROJECTS = 2
    }

    @Inject lateinit var router: Router
    @Inject lateinit var mainProjectsListManager: MainProjectsListManager
    @Inject lateinit var resourceManager: ResourceManager

    private var currentPage = 0
    private var disposable: Disposable? = null

    init {
        App.DAGGER.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        requestFirstPage()
    }

    private fun requestProjects(page: Int) {
        Timber.d("requestProjects: $page")

        if (page == 1) {
            disposable?.dispose()
            disposable = null
        }

        if (disposable == null) {
            disposable = getProjectsSingle(page)
                    .doOnSubscribe { if (page == 1) viewState.showProgress(true) else viewState.showPageProgress(true) }
                    .doOnEvent { _, _ -> if (page == 1) viewState.showProgress(false) else viewState.showPageProgress(false) }
                    .doOnEvent { _, _ -> disposable = null }
                    .subscribe(
                            { projects ->
                                Timber.d("getProjects: ${projects.size}")
                                if (projects.isNotEmpty()) {
                                    currentPage = page
                                    if (page == 1) viewState.clearData()
                                    viewState.setNewData(projects)
                                }
                            },
                            { error ->
                                Timber.e("getProjects: $error")
                                viewState.showMessage(error.userMessage(resourceManager))
                            }
                    )
        }
    }

    private fun getProjectsSingle(page: Int) = when(mode) {
        STARRED_PROJECTS -> mainProjectsListManager.getStarredProjects(page)
        MY_PROJECTS -> mainProjectsListManager.getMyProjects(page)
        else -> mainProjectsListManager.getMainProjects(page)
    }

    override fun onDestroy() {
        disposable?.dispose()
    }

    fun requestFirstPage() = requestProjects(1)
    fun requestNextPage() = requestProjects(currentPage + 1)
    fun onBackPressed() = router.exit()
}