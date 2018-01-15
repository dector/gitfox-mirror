package ru.terrakok.gitlabclient.ui.my.mergerequests

import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_my_merge_requests_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick
import javax.inject.Inject

class MyMergeRequestsContainerFragment : BaseFragment() {
    @Inject lateinit var menuController: GlobalMenuController

    override val layoutRes = R.layout.fragment_my_merge_requests_container

    private val adapter: MyMergeRequestsPagesAdapter by lazy { MyMergeRequestsPagesAdapter() }
    private var showOnlyOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.MAIN_ACTIVITY_SCOPE))
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        with(toolbar) {
            setNavigationOnClickListener { menuController.open() }
            inflateMenu(R.menu.my_mr_menu)
            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.onlyOpenedAction -> {
                        showOnlyOpened = !showOnlyOpened
                        item.isChecked = showOnlyOpened
                        childFragmentManager.fragments.forEach {
                            (it as? MyMergeRequestsFragment)?.showOnlyOpened(showOnlyOpened)
                        }
                    }
                }
                true
            }
            menu.findItem(R.id.onlyOpenedAction)?.isChecked = showOnlyOpened
        }

        toolbar.setNavigationOnClickListener { menuController.open() }
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

    private inner class MyMergeRequestsPagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        override fun getItem(position: Int) = when (position) {
            0 -> MyMergeRequestsFragment.newInstance(true, showOnlyOpened)
            1 -> MyMergeRequestsFragment.newInstance(false, showOnlyOpened)
            else -> null
        }

        override fun getCount() = 2

        override fun getPageTitle(position: Int) = when (position) {
            0 -> getString(R.string.merge_request_created_by_me)
            1 -> getString(R.string.merge_request_assigned_by_me)
            else -> null
        }
    }

    companion object {
        private const val STATE_ONLY_OPENED = "state_only_opened"
    }
}