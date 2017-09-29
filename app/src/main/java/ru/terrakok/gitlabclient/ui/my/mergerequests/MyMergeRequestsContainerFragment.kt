package ru.terrakok.gitlabclient.ui.my.mergerequests

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_my_merge_requests_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.presentation.global.GlobalMenuController
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick
import javax.inject.Inject

class MyMergeRequestsContainerFragment : BaseFragment() {
    @Inject lateinit var menuController: GlobalMenuController

    override val layoutRes = R.layout.fragment_my_merge_requests_container

    private val adapter: MyMergeRequestsPagesAdapter by lazy { MyMergeRequestsPagesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.MAIN_ACTIVITY_SCOPE))
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { menuController.open() }
        viewPager.adapter = adapter
    }

    private inner class MyMergeRequestsPagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        private val pages = listOf<Fragment>(
                MyMergeRequestsFragment.newInstance(MergeRequestState.OPENED),
                MyMergeRequestsFragment.newInstance(MergeRequestState.MERGED),
                MyMergeRequestsFragment.newInstance(MergeRequestState.CLOSED)
        )
        private val pageTitles = listOf<String>(
                getString(R.string.merge_request_opened),
                getString(R.string.merge_request_merged),
                getString(R.string.merge_request_closed)
        )

        override fun getItem(position: Int) = pages[position]

        override fun getCount() = pages.size

        override fun getPageTitle(position: Int) = pageTitles[position]
    }
}