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
class ProjectsListPresenter : MvpPresenter<ProjectsListView>() {
    @Inject
    lateinit var router: Router
    @Inject
    lateinit var serverManager: ServerManager

    private var disposable: Disposable? = null

    init {
        App.DAGGER.appComponent.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        requestProjects()
    }

    private fun requestProjects() {
        disposable = serverManager.api.getProjects()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    result -> Timber.d("getProjects: ${result.size}")
                }, {
                    error -> Timber.e("getProjects: $error")
                })
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }
}