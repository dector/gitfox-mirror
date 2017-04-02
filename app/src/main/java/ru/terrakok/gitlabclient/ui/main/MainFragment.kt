package ru.terrakok.gitlabclient.ui.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_main.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.mvp.main.MainPresenter
import ru.terrakok.gitlabclient.mvp.main.MainView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.projects.ProjectsListFragment

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 02.04.17
 */
class MainFragment : BaseFragment(), MainView {
    @InjectPresenter
    lateinit var presenter: MainPresenter

    private lateinit var adapter: MainPagesAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        adapter = MainPagesAdapter()
    }

    override fun getLayoutId() = R.layout.fragment_main

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewPager.adapter = adapter
    }

    private inner class MainPagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        private val pages = listOf<Fragment>(
                ProjectsListFragment.newInstance(ProjectsListFragment.MY_PROJECTS),
                ProjectsListFragment.newInstance(ProjectsListFragment.STARRED_PROJECTS),
                ProjectsListFragment.newInstance(ProjectsListFragment.EXPLORE_PROJECTS)
        )
        private val pageTitles = listOf<String>(
                getString(R.string.my_projects_title),
                getString(R.string.starred_projects_title),
                getString(R.string.explore_projects_title)
        )

        override fun getItem(position: Int) = pages[position]

        override fun getCount() = pages.size

        override fun getPageTitle(position: Int) = pageTitles[position]
    }

    override fun onBackPressed() = presenter.onBackPressed()
}