package ru.terrakok.gitlabclient.ui.projects

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import kotlinx.android.synthetic.main.layout_base_list.*
import kotlinx.android.synthetic.main.layout_zero.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.projects.ProjectsListPresenter
import ru.terrakok.gitlabclient.presentation.projects.ProjectsListView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectListMode
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.ZeroViewHolder
import ru.terrakok.gitlabclient.ui.global.list.ProgressAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.ProgressItem
import ru.terrakok.gitlabclient.ui.global.list.ProjectAdapterDelegate
import toothpick.Toothpick
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.03.17
 */
class ProjectsListFragment : BaseFragment(), ProjectsListView {
    companion object {
        private val ARG_MODE = "plf_mode"

        fun newInstance(mode: Int) = ProjectsListFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_MODE, mode)
            }
        }
    }

    private val adapter = ProjectsAdapter()
    private var zeroViewHolder: ZeroViewHolder? = null

    override val layoutRes = R.layout.fragment_projects

    @InjectPresenter lateinit var presenter: ProjectsListPresenter

    @ProvidePresenter
    fun createPresenter(): ProjectsListPresenter {
        val scopeName = "projects list scope"
        val scope = Toothpick.openScopes(DI.MAIN_ACTIVITY_SCOPE, scopeName)
        scope.installModules(object : Module() {
            init {
                bind(PrimitiveWrapper::class.java)
                        .withName(ProjectListMode::class.java)
                        .toInstance(PrimitiveWrapper(arguments?.getInt(ARG_MODE)))
            }
        })
        return scope.getInstance(ProjectsListPresenter::class.java).also {
            Toothpick.closeScope(scopeName)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        swipeToRefresh.setOnRefreshListener { presenter.refreshProjects() }
        zeroViewHolder = ZeroViewHolder(zeroLayout, { presenter.refreshProjects() })
    }

    override fun showRefreshProgress(show: Boolean) {
        swipeToRefresh.post { swipeToRefresh?.isRefreshing = show }
    }

    override fun showEmptyProgress(show: Boolean) {
        fullscreenProgressView.visible(show)

        //trick for disable and hide swipeToRefresh on fullscreen progress
        swipeToRefresh.visible(!show)
        swipeToRefresh.post { swipeToRefresh.isRefreshing = false }
    }

    override fun showEmptyView(show: Boolean) {
        if (show) zeroViewHolder?.showEmptyData()
        else zeroViewHolder?.hide()
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        if (show) zeroViewHolder?.showEmptyError(message)
        else zeroViewHolder?.hide()
    }

    override fun showProjects(show: Boolean, projects: List<Project>) {
        recyclerView.visible(show)
        recyclerView.post { adapter.setData(projects) }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun showPageProgress(isVisible: Boolean) {
        recyclerView.post { adapter.showProgress(isVisible) }
    }

    override fun onBackPressed() = presenter.onBackPressed()

    inner class ProjectsAdapter : ListDelegationAdapter<MutableList<Any>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(ProjectAdapterDelegate({ presenter.onProjectClicked(it.id) }))
            delegatesManager.addDelegate(ProgressAdapterDelegate())
        }

        fun setData(projects: List<Project>) {
            val progress = isProgress()

            items.clear()
            items.addAll(projects)
            if (progress) items.add(ProgressItem())

            notifyDataSetChanged()
        }

        fun showProgress(isVisible: Boolean) {
            val currentProgress = isProgress()

            if (isVisible && !currentProgress) items.add(ProgressItem())
            else if (!isVisible && currentProgress) items.remove(items.last())

            notifyDataSetChanged()
        }

        private fun isProgress() = items.isNotEmpty() && items.last() is ProgressItem

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int, payloads: MutableList<Any?>?) {
            super.onBindViewHolder(holder, position, payloads)

            if (position == items.size - 10) presenter.loadNextProjectsPage()
        }
    }
}