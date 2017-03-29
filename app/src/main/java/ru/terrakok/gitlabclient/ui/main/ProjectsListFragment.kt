package ru.terrakok.gitlabclient.ui.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_projects.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.mvp.main.ProjectsListPresenter
import ru.terrakok.gitlabclient.mvp.main.ProjectsListView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 29.03.17
 */
class ProjectsListFragment : BaseFragment(), ProjectsListView {

    @InjectPresenter
    lateinit var presenter: ProjectsListPresenter

    override fun getLayoutId() = R.layout.fragment_projects

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
    }
}