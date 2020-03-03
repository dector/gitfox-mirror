package ru.terrakok.gitlabclient.ui.my.mergerequests

import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import javax.inject.Inject
import kotlinx.android.synthetic.main.fragment_my_merge_requests_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.util.addSystemTopPadding
import toothpick.Toothpick

class MyMergeRequestsContainerFragment : BaseFragment() {

    @Inject
    lateinit var router: FlowRouter

    @Inject
    lateinit var menuController: GlobalMenuController

    override val layoutRes = R.layout.fragment_my_merge_requests_container

    private val adapter: MyMergeRequestsPagesAdapter by lazy { MyMergeRequestsPagesAdapter() }
    private var showOnlyOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.let { state ->
            showOnlyOpened = state.getBoolean(STATE_ONLY_OPENED)
        }

        with(toolbar) {
            addSystemTopPadding()
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STATE_ONLY_OPENED, showOnlyOpened)
    }

    override fun onBackPressed() {
        router.exit()
    }

    private inner class MyMergeRequestsPagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        override fun getItem(position: Int) = when (position) {
            0 -> Screens.MyMergeRequests(true, showOnlyOpened).fragment
            else -> Screens.MyMergeRequests(false, showOnlyOpened).fragment
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
