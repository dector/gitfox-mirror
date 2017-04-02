package ru.terrakok.gitlabclient.mvp.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.App
import ru.terrakok.gitlabclient.model.server.ServerManager
import timber.log.Timber
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 30.03.17
 */

@InjectViewState
class ProjectsListPresenter(private val filter: ProjectsListFilter) : MvpPresenter<ProjectsListView>() {
    @Inject
    lateinit var router: Router
    @Inject
    lateinit var serverManager: ServerManager

    private var currentPage = 0
    private var disposable: Disposable? = null

    init {
        App.DAGGER.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        requestFirstPage()
    }

    private fun requestProjects(page: Int) {
        Timber.d("requestProjects: $page")

        if (page == 1) {
            disposable?.dispose()
            disposable = null
        }

        if (disposable == null) {
            disposable = serverManager.api.getProjects(
                    filter.archived,
                    filter.visibility,
                    filter.order_by,
                    filter.sort,
                    filter.search,
                    filter.simple,
                    filter.owned,
                    filter.membership,
                    filter.starred,
                    page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe { if (page == 1) viewState.showProgress(true) else viewState.showPageProgress(true) }
                    .doOnEvent { _, _ -> if (page == 1) viewState.showProgress(false) else viewState.showPageProgress(false) }
                    .doOnEvent { _, _ -> disposable = null }
                    .subscribe({
                        result ->
                        Timber.d("getProjects: ${result.size}")
                        if (result.isNotEmpty()) {
                            currentPage = page
                            if (page == 1) viewState.clearData()
                            viewState.setNewData(result)
                        }
                    }, {
                        error ->
                        Timber.e("getProjects: $error")
                    })
        }
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

    fun requestFirstPage() = requestProjects(1)
    fun requestNextPage() = requestProjects(currentPage + 1)
    fun onBackPressed() = router.exit()
}