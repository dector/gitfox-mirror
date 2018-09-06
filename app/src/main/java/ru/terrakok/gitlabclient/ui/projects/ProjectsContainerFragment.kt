package ru.terrakok.gitlabclient.ui.projects

import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_my_issues_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import ru.terrakok.gitlabclient.presentation.projects.ProjectsListPresenter
import ru.terrakok.gitlabclient.toothpick.DI
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
        Toothpick.inject(this, Toothpick.openScope(DI.DRAWER_FLOW_SCOPE))
        super.onCreate(savedInstanceState)
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
            0 -> Screens.createFragment(Screens.PROJECTS_SCREEN, ProjectsListPresenter.MAIN_PROJECTS)
            1 -> Screens.createFragment(Screens.PROJECTS_SCREEN, ProjectsListPresenter.MY_PROJECTS)
            2 -> Screens.createFragment(Screens.PROJECTS_SCREEN, ProjectsListPresenter.STARRED_PROJECTS)
            else -> null
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