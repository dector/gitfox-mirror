package ru.terrakok.gitlabclient.ui.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_projects.*
import kotlinx.android.synthetic.main.item_project.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.mvp.main.ProjectsListPresenter
import ru.terrakok.gitlabclient.mvp.main.ProjectsListView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.03.17
 */
class ProjectsListFragment : BaseFragment(), ProjectsListView {

    @InjectPresenter
    lateinit var presenter: ProjectsListPresenter

    private val adapter = ProjectsAdapter()

    override fun getLayoutId() = R.layout.fragment_projects

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
        swipeToRefresh.post { swipeToRefresh.isRefreshing = isVisible }
    }

    override fun showPageProgress(isVisible: Boolean) {
        //
    }

    inner class ProjectsAdapter : RecyclerView.Adapter<ProjectsAdapter.ProjectVH>() {
        private var projects: MutableList<Project> = mutableListOf()

        fun clearData() {
            projects = mutableListOf()
            notifyDataSetChanged()
        }

        fun addData(newProjects: List<Project>) {
            projects.addAll(newProjects)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                = ProjectVH(LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false))

        override fun getItemCount() = projects.size

        override fun onBindViewHolder(holder: ProjectVH, position: Int) {
            holder.bind(projects[position])

            if (position == projects.size - 5) {
                presenter.requestNextPage()
            }
        }

        inner class ProjectVH(val view: View) : RecyclerView.ViewHolder(view) {
            fun bind(project: Project) {
                view.projectTitleTV.text = project.nameWithNamespace
            }
        }
    }
}