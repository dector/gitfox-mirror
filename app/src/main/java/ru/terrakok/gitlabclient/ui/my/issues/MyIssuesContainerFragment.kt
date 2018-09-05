package ru.terrakok.gitlabclient.ui.my.issues

import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_my_issues_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 16.07.17.
 */
class MyIssuesContainerFragment : BaseFragment() {
    @Inject
    lateinit var menuController: GlobalMenuController

    override val layoutRes = R.layout.fragment_my_issues_container

    private val adapter: MyIssuesPagesAdapter by lazy { MyIssuesPagesAdapter() }
    private var showOnlyOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.DRAWER_FLOW_SCOPE))
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(toolbar) {
            setNavigationOnClickListener { menuController.open() }
            inflateMenu(R.menu.my_issues_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.onlyOpenedAction -> {
                        showOnlyOpened = !showOnlyOpened
                        item.isChecked = showOnlyOpened
                        childFragmentManager.fragments.forEach {
                            (it as? MyIssuesFragment)?.showOnlyOpened(showOnlyOpened)
                        }
                    }
                }
                true
            }
            menu.findItem(R.id.onlyOpenedAction)?.isChecked = showOnlyOpened
        }

        viewPager.adapter = adapter
    }

    override fun restoreState(state: Bundle) {
        super.restoreState(state)
        showOnlyOpened = state.getBoolean(STATE_ONLY_OPENED)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_ONLY_OPENED, showOnlyOpened)
    }

    private inner class MyIssuesPagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        override fun getItem(position: Int) = when (position) {
            0 -> MyIssuesFragment.newInstance(true, showOnlyOpened)
            1 -> MyIssuesFragment.newInstance(false, showOnlyOpened)
            else -> null
        }

        override fun getCount() = 2

        override fun getPageTitle(position: Int) = when (position) {
            0 -> getString(R.string.issues_created_by_me)
            1 -> getString(R.string.issues_assigned_by_me)
            else -> null
        }
    }

    companion object {
        private const val STATE_ONLY_OPENED = "state_only_opened"
    }
}