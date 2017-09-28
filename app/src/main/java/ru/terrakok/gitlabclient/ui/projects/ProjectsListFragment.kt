package ru.terrakok.gitlabclient.ui.projects

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_projects.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.presentation.projects.ProjectsListPresenter
import ru.terrakok.gitlabclient.presentation.projects.ProjectsListView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.ProjectListMode
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.ListItem
import ru.terrakok.gitlabclient.ui.global.list.ProgressAdapterDelegate
import ru.terrakok.gitlabclient.ui.global.list.ProjectAdapterDelegate
import toothpick.Toothpick
import toothpick.config.Module

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.03.17
 */
class ProjectsListFragment : BaseFragment(), ProjectsListView {
    companion object {
        private val ARG_MODE = "plf_mode"

        fun newInstance(mode: Int): ProjectsListFragment {
            val fragment = ProjectsListFragment()

            val bundle = Bundle()
            bundle.putInt(ARG_MODE, mode)
            fragment.arguments = bundle

            return fragment
        }
    }

    private val adapter = ProjectsAdapter()

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
                        .toInstance(PrimitiveWrapper(arguments.getInt(ARG_MODE)))
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
    }

    override fun showRefreshProgress(show: Boolean) {
        swipeToRefresh.post { swipeToRefresh?.isRefreshing = show }
    }

    override fun showEmptyProgress(show: Boolean) {
        swipeToRefresh.post { swipeToRefresh?.isRefreshing = show }
    }

    override fun showEmptyView(show: Boolean) {
        //todo
    }

    override fun showEmptyError(show: Boolean, message: String?) {
        if (show && message != null) showSnackMessage(message)
    }

    override fun showProjects(show: Boolean, projects: List<Project>) {
        recyclerView.post { adapter.setData(projects) }
    }

    override fun onBackPressed() = presenter.onBackPressed()

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun showPageProgress(isVisible: Boolean) {
        recyclerView.post { adapter.showProgress(isVisible) }
    }

    inner class ProjectsAdapter : ListDelegationAdapter<MutableList<ListItem>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(ProjectAdapterDelegate({ presenter.onProjectClicked(it.id) }))
            delegatesManager.addDelegate(ProgressAdapterDelegate())
        }

        fun setData(projects: List<Project>) {
            val progress = isProgress()

            items.clear()
            items.addAll(projects.map { ListItem.ProjectItem(it) })
            if (progress) items.add(ListItem.ProgressItem())

            notifyDataSetChanged()
        }

        fun showProgress(isVisible: Boolean) {
            val currentProgress = isProgress()

            if (isVisible && !currentProgress) items.add(ListItem.ProgressItem())
            else if (!isVisible && currentProgress) items.remove(items.last())

            notifyDataSetChanged()
        }

        private fun isProgress() = items.isNotEmpty() && items.last() is ListItem.ProgressItem

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int, payloads: MutableList<Any?>?) {
            super.onBindViewHolder(holder, position, payloads)

            if (position == items.size - 10) presenter.loadNextProjectsPage()
        }
    }
}