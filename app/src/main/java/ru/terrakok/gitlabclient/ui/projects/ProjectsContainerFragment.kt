package ru.terrakok.gitlabclient.ui.projects

import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_my_issues_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import ru.terrakok.gitlabclient.presentation.projects.ProjectsListPresenter
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick
import javax.inject.Inject


class ProjectsContainerFragment : BaseFragment() {
    @Inject
    lateinit var menuController: GlobalMenuController
    @Inject
    lateinit var router: FlowRouter

    override val layoutRes = R.layout.fragment_projects_container

    private val adapter: ProjectsPagesAdapter by lazy { ProjectsPagesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { menuController.open() }
        viewPager.adapter = adapter
    }

    override fun onBackPressed() {
        router.exit()
    }

    private inner class ProjectsPagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        override fun getItem(position: Int) = when (position) {
            0 -> Screens.Projects(ProjectsListPresenter.MAIN_PROJECTS).fragment
            1 -> Screens.Projects(ProjectsListPresenter.MY_PROJECTS).fragment
            else -> Screens.Projects(ProjectsListPresenter.STARRED_PROJECTS).fragment
        }

        override fun getCount() = 3

        override fun getPageTitle(position: Int) = when (position) {
            0 -> getString(R.string.all_projects_title)
            1 -> getString(R.string.my_projects_title)
            2 -> getString(R.string.starred_projects_title)
            else -> null
        }
    }
}