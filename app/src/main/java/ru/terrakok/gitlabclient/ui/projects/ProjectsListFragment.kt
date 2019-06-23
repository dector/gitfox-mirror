package ru.terrakok.gitlabclient.ui.projects

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.layout_base_list.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.di.ProjectListMode
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.projects.ProjectsListPresenter
import ru.terrakok.gitlabclient.presentation.projects.ProjectsListView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Scope
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.03.17
 */
class ProjectsListFragment : BaseFragment(), ProjectsListView {

    private val adapter = ProjectsAdapter({ presenter.loadNextProjectsPage() }, { presenter.onProjectClicked(it) })

    override val layoutRes = R.layout.fragment_projects

    override fun installModules(scope: Scope) {
        scope.installModules(object : Module() {
            init {
                bind(PrimitiveWrapper::class.java)
                    .withName(ProjectListMode::class.java)
                    .toInstance(PrimitiveWrapper(arguments?.getInt(ARG_MODE)))
            }
        })
    }

    @InjectPresenter
    lateinit var presenter: ProjectsListPresenter

    @ProvidePresenter
    fun createPresenter(): ProjectsListPresenter =
        scope.getInstance(ProjectsListPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        swipeToRefresh.setOnRefreshListener { presenter.refreshProjects() }
        emptyView.setRefreshListener { presenter.refreshProjects() }
    }

    override fun showRefreshProgress(show: Boolean) {
        postViewAction { swipeToRefresh.isRefreshing = show }
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)

        // Trick for disable and hide swipeToRefresh on fullscreen progress
        swipeToRefresh.visible(!show)
        postViewAction { swipeToRefresh.isRefreshing = false }
    }

    override fun showEmptyView(show: Boolean) {
        emptyView.apply { if (show) showEmptyData() else hide() }
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        emptyView.apply { if (show) showEmptyError(message) else hide() }
    }

    override fun showProjects(show: Boolean, projects: List<Project>) {
        recyclerView.visible(show)
        postViewAction { adapter.setData(projects) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun showPageProgress(isVisible: Boolean) {
        postViewAction { adapter.showProgress(isVisible) }
    }

    override fun onBackPressed() = presenter.onBackPressed()

    companion object {
        private const val ARG_MODE = "plf_mode"

        fun create(mode: Int) = ProjectsListFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_MODE, mode)
            }
        }
    }
}