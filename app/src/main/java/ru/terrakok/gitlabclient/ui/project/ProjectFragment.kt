package ru.terrakok.gitlabclient.ui.project

import android.os.Bundle
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import kotlinx.android.synthetic.main.fragment_project.*
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.argument
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.extension.shareText
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.project.info.ProjectInfoFragment
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 10.02.18.
 */
class ProjectFragment : BaseFragment(), ProjectInfoFragment.ProjectInfoToolbar {
    override val layoutRes: Int = R.layout.fragment_project
    private val scopeName: String? by argument(ARG_SCOPE_NAME)

    private val infoTab by lazy { Screens.ProjectInfoContainer(scopeName!!) }
    private val issuesTab by lazy { Screens.ProjectIssuesContainer(scopeName!!) }
    private val mrsTab by lazy { Screens.ProjectMergeRequestsContainer(scopeName!!) }

    private val currentTabFragment: BaseFragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden } as? BaseFragment

    @Inject
    lateinit var router: FlowRouter

    private var shareUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(scopeName))
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.apply {
            setNavigationOnClickListener { onBackPressed() }
            inflateMenu(R.menu.share_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.shareAction -> shareText(shareUrl)
                }
                true
            }
        }
        AHBottomNavigationAdapter(activity, R.menu.project_bottom_menu).apply {
            setupWithBottomNavigation(bottomBar)
        }
        with(bottomBar) {
            accentColor = context.color(R.color.colorPrimary)
            inactiveColor = context.color(R.color.silver)

            setOnTabSelectedListener { position, wasSelected ->
                if (!wasSelected) selectTab(
                    when (position) {
                        0 -> infoTab
                        1 -> issuesTab
                        else -> mrsTab
                    }
                )
                true
            }
        }

        selectTab(
            when (currentTabFragment?.tag) {
                infoTab.screenKey -> infoTab
                issuesTab.screenKey -> issuesTab
                mrsTab.screenKey -> mrsTab
                else -> infoTab
            }
        )
    }

    private fun selectTab(tab: SupportAppScreen) {
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(tab.screenKey)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return

        childFragmentManager.beginTransaction().apply {
            if (newFragment == null) add(R.id.projectMainContainer, tab.fragment, tab.screenKey)

            currentFragment?.let {
                hide(it)
                it.userVisibleHint = false
            }
            newFragment?.let {
                show(it)
                it.userVisibleHint = true
            }
        }.commitNow()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        router.exit()
    }

    override fun setTitle(title: String) {
        toolbar.title = title
    }

    override fun setShareUrl(url: String?) {
        shareUrl = url
    }

    companion object {
        private const val ARG_SCOPE_NAME = "arg_scope_name"
        fun create(scope: String) =
            ProjectFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_SCOPE_NAME, scope)
                }
            }
    }
}