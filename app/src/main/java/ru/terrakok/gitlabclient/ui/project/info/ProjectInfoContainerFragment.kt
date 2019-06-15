package ru.terrakok.gitlabclient.ui.project.info

import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_project_info_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick

class ProjectInfoContainerFragment : BaseFragment() {

    @Inject
    lateinit var router: FlowRouter

    override val layoutRes = R.layout.fragment_project_info_container

    private val adapter: ProjectInfoPagesAdapter by lazy { ProjectInfoPagesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewPager.adapter = adapter
    }

    override fun onBackPressed() {
        router.exit()
    }

    private inner class ProjectInfoPagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        override fun getItem(position: Int): BaseFragment = when (position) {
            0 -> Screens.ProjectInfo.fragment
            1 -> Screens.ProjectEvents.fragment
            2 -> Screens.ProjectLabels.fragment
            else -> Screens.ProjectMilestones.fragment
        }

        override fun getCount() = 4

        override fun getPageTitle(position: Int) = when (position) {
            0 -> getString(R.string.project_info)
            1 -> getString(R.string.project_events)
            2 -> getString(R.string.project_labels)
            3 -> getString(R.string.project_milestones)
            else -> null
        }
    }
}