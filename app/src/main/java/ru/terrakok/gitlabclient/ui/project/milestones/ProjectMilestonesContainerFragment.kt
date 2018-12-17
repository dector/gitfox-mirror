package ru.terrakok.gitlabclient.ui.project.milestones

import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_my_merge_requests_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.milestone.MilestoneState
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 *  @author Valentin Logvinovitch (glvvl) on 17.12.18.
 */
class ProjectMilestonesContainerFragment : BaseFragment() {

    override val layoutRes = R.layout.fragment_project_milestones_container

    private val adapter: ProjectMilestonesPagesAdapter by lazy { ProjectMilestonesPagesAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewPager.adapter = adapter
    }

    private inner class ProjectMilestonesPagesAdapter :
        FragmentPagerAdapter(childFragmentManager) {

        override fun getItem(position: Int) = when (position) {
            0 -> Screens.ProjectMilestones(MilestoneState.ACTIVE).fragment
            1 -> Screens.ProjectMilestones(MilestoneState.CLOSED).fragment
            else -> null
        }

        override fun getCount() = 2

        override fun getPageTitle(position: Int) = when (position) {
            0 -> getString(R.string.target_status_opened)
            1 -> getString(R.string.target_status_closed)
            else -> null
        }
    }
}