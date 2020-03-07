package ru.terrakok.gitlabclient.ui.project.mergerequest

import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_my_merge_requests_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.MergeRequestState
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 28.08.18
 */
class ProjectMergeRequestsContainerFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_project_merge_requests_container

    private val adapter: ProjectMergeRequestsPagesAdapter by lazy { ProjectMergeRequestsPagesAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewPager.adapter = adapter
    }

    private inner class ProjectMergeRequestsPagesAdapter :
        FragmentPagerAdapter(childFragmentManager) {

        override fun getItem(position: Int) = when (position) {
            0 -> Screens.ProjectMergeRequests(MergeRequestState.OPENED).fragment
            1 -> Screens.ProjectMergeRequests(MergeRequestState.MERGED).fragment
            else -> Screens.ProjectMergeRequests(MergeRequestState.CLOSED).fragment
        }

        override fun getCount() = 3

        override fun getPageTitle(position: Int) = when (position) {
            0 -> getString(R.string.target_status_opened)
            1 -> getString(R.string.target_status_merged)
            2 -> getString(R.string.target_status_closed)
            else -> null
        }
    }
}
