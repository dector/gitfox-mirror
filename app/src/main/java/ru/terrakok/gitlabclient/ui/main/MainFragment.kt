package ru.terrakok.gitlabclient.ui.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_main.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.main.MainPresenter
import ru.terrakok.gitlabclient.presentation.main.MainView
import ru.terrakok.gitlabclient.presentation.projects.ProjectsListPresenter
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.projects.ProjectsListFragment

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 02.04.17
 */
class MainFragment : BaseFragment(), MainView {
    @InjectPresenter lateinit var presenter: MainPresenter

    private lateinit var adapter: MainPagesAdapter

    override val layoutRes = R.layout.fragment_main

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        adapter = MainPagesAdapter()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { presenter.onMenuPressed() }
        viewPager.adapter = adapter
    }

    private inner class MainPagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        private val pages = listOf<Fragment>(
                ProjectsListFragment.newInstance(ProjectsListPresenter.MAIN_PROJECTS),
                ProjectsListFragment.newInstance(ProjectsListPresenter.MY_PROJECTS),
                ProjectsListFragment.newInstance(ProjectsListPresenter.STARRED_PROJECTS)
        )
        private val pageTitles = listOf<String>(
                getString(R.string.all_projects_title),
                getString(R.string.my_projects_title),
                getString(R.string.starred_projects_title)
        )

        override fun getItem(position: Int) = pages[position]

        override fun getCount() = pages.size

        override fun getPageTitle(position: Int) = pageTitles[position]
    }

    override fun onBackPressed() = presenter.onBackPressed()
}