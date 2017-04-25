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
import ru.terrakok.gitlabclient.mvp.projects.ProjectsListPresenter
import ru.terrakok.gitlabclient.mvp.projects.ProjectsListView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

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
    @ProvidePresenter fun createPresenter() = ProjectsListPresenter(arguments.getInt(ARG_MODE))

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        swipeToRefresh.setOnRefreshListener { presenter.requestFirstPage() }
    }

    override fun onBackPressed() = presenter.onBackPressed()

    override fun clearData() {
        adapter.clearData()
    }

    override fun setNewData(projects: List<Project>) {
        adapter.addData(projects)
    }

    override fun showProgress(isVisible: Boolean) {
        swipeToRefresh.post { swipeToRefresh?.isRefreshing = isVisible }
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun showPageProgress(isVisible: Boolean) {
        adapter.showProgress(isVisible)
    }

    inner class ProjectsAdapter : ListDelegationAdapter<MutableList<ProjectsListItem>>() {
        init {
            items = mutableListOf()
            delegatesManager.addDelegate(ProjectAdapterDelegate())
            delegatesManager.addDelegate(ProgressAdapterDelegate())
        }

        fun clearData() {
            items = if (isProgress()) mutableListOf(ProjectsListItem.ProgressItem()) else mutableListOf()
            notifyDataSetChanged()
        }

        fun addData(newProjects: List<Project>) {
            items.addAll(
                    if (isProgress()) items.size - 1 else items.size,
                    newProjects.map { ProjectsListItem.ProjectItem(it) }
            )
            notifyDataSetChanged()
        }

        fun showProgress(isVisible: Boolean) {
            val currentProgress = isProgress()

            if (isVisible && !currentProgress) items.add(ProjectsListItem.ProgressItem())
            else if (!isVisible && currentProgress) items.remove(items.last())

            notifyDataSetChanged()
        }

        private fun isProgress() = items.isNotEmpty() && items.last() is ProjectsListItem.ProgressItem

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int, payloads: MutableList<Any?>?) {
            super.onBindViewHolder(holder, position, payloads)

            if (position == items.size - 10) presenter.requestNextPage()
        }
    }
}