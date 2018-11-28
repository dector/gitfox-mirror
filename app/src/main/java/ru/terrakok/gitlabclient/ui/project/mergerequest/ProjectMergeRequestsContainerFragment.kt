package ru.terrakok.gitlabclient.ui.project.mergerequest

import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_my_merge_requests_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 28.08.18
 */
class ProjectMergeRequestsContainerFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_project_merge_requests_container
    private val scopeName: String? by argument(ARG_SCOPE_NAME)

    private val adapter: ProjectMergeRequestsPagesAdapter by lazy { ProjectMergeRequestsPagesAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewPager.adapter = adapter
    }

    private inner class ProjectMergeRequestsPagesAdapter :
        FragmentPagerAdapter(childFragmentManager) {

        override fun getItem(position: Int) = when (position) {
            0 -> Screens.ProjectMergeRequests(MergeRequestState.OPENED, scopeName!!).fragment
            1 -> Screens.ProjectMergeRequests(MergeRequestState.MERGED, scopeName!!).fragment
            2 -> Screens.ProjectMergeRequests(MergeRequestState.CLOSED, scopeName!!).fragment
            else -> null
        }

        override fun getCount() = 3

        override fun getPageTitle(position: Int) = when (position) {
            0 -> getString(R.string.target_status_opened)
            1 -> getString(R.string.target_status_merged)
            2 -> getString(R.string.target_status_closed)
            else -> null
        }
    }

    companion object {
        private const val ARG_SCOPE_NAME = "arg_scope_name"
        fun create(scope: String) =
            ProjectMergeRequestsContainerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SCOPE_NAME, scope)
                }
            }
    }
}