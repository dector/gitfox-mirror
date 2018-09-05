package ru.terrakok.gitlabclient.ui.main

import android.os.Bundle
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.my.activity.MyEventsFragment
import ru.terrakok.gitlabclient.ui.my.issues.MyIssuesContainerFragment
import ru.terrakok.gitlabclient.ui.my.mergerequests.MyMergeRequestsContainerFragment
import ru.terrakok.gitlabclient.ui.my.todos.MyTodosContainerFragment
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok). Date: 02.04.17
 */
class MainFragment : BaseFragment() {
    override val layoutRes = R.layout.fragment_main

    private lateinit var tabs: HashMap<String, BaseFragment>
    private val tabKeys = listOf(
        tabIdToFragmentTag(R.id.tab_activity),
        tabIdToFragmentTag(R.id.tab_issue),
        tabIdToFragmentTag(R.id.tab_merge),
        tabIdToFragmentTag(R.id.tab_todo)
    )

    @Inject
    lateinit var router: FlowRouter

    private fun tabIdToFragmentTag(id: Int) = "tab_$id"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        AHBottomNavigationAdapter(activity, R.menu.main_bottom_menu).apply {
            setupWithBottomNavigation(bottomBar)
        }
        with(bottomBar) {
            accentColor = context.color(R.color.colorPrimary)
            inactiveColor = context.color(R.color.silver)

            setOnTabSelectedListener { position, wasSelected ->
                if (!wasSelected) showTab(position, currentItem)
                true
            }
        }

        if (savedInstanceState == null) {
            tabs = createNewFragments()
            childFragmentManager.beginTransaction()
                .add(R.id.mainScreenContainer, tabs[tabKeys[0]], tabKeys[0])
                .add(R.id.mainScreenContainer, tabs[tabKeys[1]], tabKeys[1])
                .add(R.id.mainScreenContainer, tabs[tabKeys[2]], tabKeys[2])
                .add(R.id.mainScreenContainer, tabs[tabKeys[3]], tabKeys[3])
                .hide(tabs[tabKeys[1]])
                .hide(tabs[tabKeys[2]])
                .hide(tabs[tabKeys[3]])
                .commitNow()
            bottomBar.setCurrentItem(0, false)
        } else {
            tabs = findFragments()
        }
    }

    private fun showTab(newItem: Int, oldItem: Int) {
        childFragmentManager.beginTransaction()
            .hide(tabs[tabKeys[oldItem]])
            .show(tabs[tabKeys[newItem]])
            .commit()
    }

    private fun createNewFragments(): HashMap<String, BaseFragment> = hashMapOf(
        tabKeys[0] to MyEventsFragment(),
        tabKeys[1] to MyIssuesContainerFragment(),
        tabKeys[2] to MyMergeRequestsContainerFragment(),
        tabKeys[3] to MyTodosContainerFragment()
    )

    private fun findFragments(): HashMap<String, BaseFragment> = hashMapOf(
        tabKeys[0] to childFragmentManager.findFragmentByTag(tabKeys[0]) as BaseFragment,
        tabKeys[1] to childFragmentManager.findFragmentByTag(tabKeys[1]) as BaseFragment,
        tabKeys[2] to childFragmentManager.findFragmentByTag(tabKeys[2]) as BaseFragment,
        tabKeys[3] to childFragmentManager.findFragmentByTag(tabKeys[3]) as BaseFragment
    )

    override fun onBackPressed() {
        router.exit()
    }
}