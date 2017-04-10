package ru.terrakok.gitlabclient.ui.projects

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import kotlinx.android.synthetic.main.fragment_projects.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.mvp.projects.ProjectsListFilter
import ru.terrakok.gitlabclient.mvp.projects.ProjectsListPresenter
import ru.terrakok.gitlabclient.mvp.projects.ProjectsListView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.03.17
 */
class ProjectsListFragment : BaseFragment(), ProjectsListView {
    companion object {
        private val ARG_TYPE = "plf_type"
        val MY_PROJECTS = 1
        val STARRED_PROJECTS = 2
        val EXPLORE_PROJECTS = 3

        fun newInstance(type: Int): ProjectsListFragment {
            val fragment = ProjectsListFragment()

            val bundle = Bundle()
            bundle.putInt(ARG_TYPE, type)
            fragment.arguments = bundle

            return fragment
        }
    }

    @InjectPresenter lateinit var presenter: ProjectsListPresenter

    @ProvidePresenter
    fun createPresenter(): ProjectsListPresenter {
        val filter = when(arguments.getInt(ARG_TYPE)) {
            MY_PROJECTS -> ProjectsListFilter(membership = true, order_by = OrderBy.LAST_ACTIVITY_AT, starred = false)
            STARRED_PROJECTS -> ProjectsListFilter(starred = true, order_by = OrderBy.LAST_ACTIVITY_AT)
            else -> ProjectsListFilter(order_by = OrderBy.LAST_ACTIVITY_AT)
        }

        return ProjectsListPresenter(filter)
    }

    private val adapter = ProjectsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater.inflate(R.layout.fragment_projects, container, false)

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