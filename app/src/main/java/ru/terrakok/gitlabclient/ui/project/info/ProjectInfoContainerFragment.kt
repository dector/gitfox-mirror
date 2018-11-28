package ru.terrakok.gitlabclient.ui.project.info

import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_project_info_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick
import javax.inject.Inject

class ProjectInfoContainerFragment : BaseFragment() {

    @Inject
    lateinit var router: FlowRouter

    override val layoutRes = R.layout.fragment_project_info_container
    private val scopeName: String? by argument(ARG_SCOPE_NAME)

    private val adapter: ProjectInfoPagesAdapter by lazy { ProjectInfoPagesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(scopeName))
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewPager.adapter = adapter
    }

    override fun onBackPressed() {
        router.exit()
    }

    private inner class ProjectInfoPagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        override fun getItem(position: Int): BaseFragment? = when (position) {
            0 -> Screens.ProjectInfo(scopeName!!).fragment
            1 -> Screens.ProjectEvents(scopeName!!).fragment
            else -> null
        }

        override fun getCount() = 2

        override fun getPageTitle(position: Int) = when (position) {
            0 -> getString(R.string.project_info)
            1 -> getString(R.string.project_events)
            else -> null
        }
    }

    companion object {
        private const val ARG_SCOPE_NAME = "arg_scope_name"
        fun create(scope: String) =
            ProjectInfoContainerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SCOPE_NAME, scope)
                }
            }
    }
}