package ru.terrakok.gitlabclient.ui.main

import android.os.Bundle
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import ru.terrakok.cicerone.android.support.SupportAppScreen
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 02.04.17
 */
class MainFlowFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_main

    private val currentTabFragment: BaseFragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden } as? BaseFragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        AHBottomNavigationAdapter(activity, R.menu.main_bottom_menu).apply {
            setupWithBottomNavigation(bottomBar)
        }
        with(bottomBar) {
            accentColor = context.color(R.color.colorPrimary)
            inactiveColor = context.color(R.color.silver)

            setOnTabSelectedListener { position, wasSelected ->
                if (!wasSelected) selectTab(
                    when (position) {
                        0 -> eventsTab
                        1 -> issuesTab
                        2 -> mrsTab
                        else -> todosTab
                    }
                )
                true
            }
        }

        selectTab(
            when (currentTabFragment?.tag) {
                eventsTab.screenKey -> eventsTab
                issuesTab.screenKey -> issuesTab
                mrsTab.screenKey -> mrsTab
                todosTab.screenKey -> todosTab
                else -> eventsTab
            }
        )
    }

    private fun selectTab(tab: SupportAppScreen) {
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(tab.screenKey)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return

        childFragmentManager.beginTransaction().apply {
            if (newFragment == null) add(R.id.mainScreenContainer, createTabFragment(tab), tab.screenKey)

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

    private fun createTabFragment(tab: SupportAppScreen) = tab.fragment

    override fun onBackPressed() {
        currentTabFragment?.onBackPressed()
    }

    companion object {
        private val eventsTab = Screens.MyEvents
        private val issuesTab = Screens.MyIssuesContainer
        private val mrsTab = Screens.MyMrContainer
        private val todosTab = Screens.MyTodosContainer
    }
}