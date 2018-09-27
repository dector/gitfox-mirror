package ru.terrakok.gitlabclient.ui.project.issues

import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_my_issues_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * @author Eugene Shapovalov (CraggyHaggy). Date: 27.08.18
 */
class ProjectIssuesContainerFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_project_issues_container
    private val scopeName: String? by argument(ARG_SCOPE_NAME)

    private val adapter: ProjectIssuesPagesAdapter by lazy { ProjectIssuesPagesAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewPager.adapter = adapter
    }

    private inner class ProjectIssuesPagesAdapter : FragmentPagerAdapter(childFragmentManager) {

        override fun getItem(position: Int) = when (position) {
            0 -> Screens.createFragment(Screens.PROJECT_ISSUES_SCREEN, Pair(IssueState.OPENED, scopeName))
            1 -> Screens.createFragment(Screens.PROJECT_ISSUES_SCREEN, Pair(IssueState.CLOSED, scopeName))
            else -> null
        }

        override fun getCount() = 2

        override fun getPageTitle(position: Int) = when (position) {
            0 -> getString(R.string.target_status_opened)
            1 -> getString(R.string.target_status_closed)
            else -> null
        }
    }

    companion object {
        private const val ARG_SCOPE_NAME = "arg_scope_name"
        fun create(scope: String) =
            ProjectIssuesContainerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SCOPE_NAME, scope)
                }
            }
    }
}