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
    @Inject lateinit var menuController: GlobalMenuController

    override val layoutRes = R.layout.fragment_my_issues_container

    private val adapter: MyIssuesPagesAdapter by lazy { MyIssuesPagesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.MAIN_ACTIVITY_SCOPE))
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        toolbar.setNavigationOnClickListener { menuController.open() }
        viewPager.adapter = adapter
    }

    private inner class MyIssuesPagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        private val pages = listOf(
                MyIssuesFragment.newInstance(true),
                MyIssuesFragment.newInstance(false)
        )
        private val pageTitles = listOf(
                getString(R.string.issues_created_by_me),
                getString(R.string.issues_assigned_by_me)
        )

        override fun getItem(position: Int) = pages[position]

        override fun getCount() = pages.size

        override fun getPageTitle(position: Int) = pageTitles[position]
    }
}