package ru.terrakok.gitlabclient.ui.project

import android.os.Bundle
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import kotlinx.android.synthetic.main.fragment_project.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 10.02.18.
 */
class ProjectMainFlowFragment : BaseFragment() {
    override val layoutRes: Int = R.layout.fragment_project

    private val currentTabFragment: BaseFragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden } as? BaseFragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        AHBottomNavigationAdapter(activity, R.menu.project_bottom_menu).apply {
            setupWithBottomNavigation(bottomBar)
        }
        with(bottomBar) {
            accentColor = context.color(R.color.colorPrimary)
            inactiveColor = context.color(R.color.silver)

            setOnTabSelectedListener { position, wasSelected ->
                if (!wasSelected) selectTab(
                    when (position) {
                        0 -> Screens.PROJECT_INFO_SCREEN
                        1 -> Screens.PROJECT_ISSUES_SCREEN
                        else -> Screens.PROJECT_MR_SCREEN
                    }
                )
                true
            }
        }

        selectTab(currentTabFragment?.tag ?: Screens.PROJECT_INFO_SCREEN)
    }

    private fun selectTab(tab: String) {
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(tab)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return

        childFragmentManager.beginTransaction().apply {
            if (newFragment == null) add(R.id.container, createTabFragment(tab), tab)

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

    private fun createTabFragment(tab: String) =
        Screens.createFragment(tab) ?: throw RuntimeException("Unknown tab $tab")

    override fun onBackPressed() {
        currentTabFragment?.onBackPressed()
    }
}