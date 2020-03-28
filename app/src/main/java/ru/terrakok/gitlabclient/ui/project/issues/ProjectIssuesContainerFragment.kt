package ru.terrakok.gitlabclient.ui.project.issues

import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import gitfox.entity.IssueState
import kotlinx.android.synthetic.main.fragment_my_issues_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 27.08.18
 */
class ProjectIssuesContainerFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_project_issues_container

    private val adapter: ProjectIssuesPagesAdapter by lazy { ProjectIssuesPagesAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewPager.adapter = adapter
    }

    private inner class ProjectIssuesPagesAdapter : FragmentPagerAdapter(childFragmentManager) {

        override fun getItem(position: Int) = when (position) {
            0 -> Screens.ProjectIssues(IssueState.OPENED).fragment
            else -> Screens.ProjectIssues(IssueState.CLOSED).fragment
        }

        override fun getCount() = 2

        override fun getPageTitle(position: Int) = when (position) {
            0 -> getString(R.string.target_status_opened)
            1 -> getString(R.string.target_status_closed)
            else -> null
        }
    }
}
