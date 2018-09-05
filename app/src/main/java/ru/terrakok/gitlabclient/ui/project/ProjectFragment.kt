package ru.terrakok.gitlabclient.ui.project

import android.os.Bundle
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import kotlinx.android.synthetic.main.fragment_project.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.project.info.ProjectInfoFragment

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 10.02.18.
 */
class ProjectFragment : BaseFragment() {

    override val layoutRes: Int = R.layout.fragment_project

    private lateinit var tabs: HashMap<String, BaseFragment>
    private val tabKeys = listOf(
        tabIdToFragmentTag(R.id.tab_info),
        tabIdToFragmentTag(R.id.tab_issue),
        tabIdToFragmentTag(R.id.tab_merge_request)
    )

    private fun tabIdToFragmentTag(id: Int) = "tab_$id"

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        AHBottomNavigationAdapter(activity, R.menu.project_bottom_menu).apply {
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
                .add(R.id.container, tabs[tabKeys[0]], tabKeys[0])
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
        tabKeys[0] to ProjectInfoFragment()
    )

    private fun findFragments(): HashMap<String, BaseFragment> = hashMapOf(
        tabKeys[0] to childFragmentManager.findFragmentByTag(tabKeys[0]) as BaseFragment
    )

    override fun onBackPressed() {
        (childFragmentManager.findFragmentById(R.id.container) as? BaseFragment)?.onBackPressed()
    }
}