package ru.terrakok.gitlabclient.ui.my.issues

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_my_issues_container.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 16.07.17.
 */
class MyIssuesContainerFragment : BaseFragment() {

    override val layoutRes = R.layout.fragment_my_issues_container

    private val adapter: MyIssuesPagesAdapter by lazy { MyIssuesPagesAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewPager.adapter = adapter
    }

    private inner class MyIssuesPagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        private val pages = listOf<Fragment>(
                MyIssuesFragment.newInstance(true),
                MyIssuesFragment.newInstance(false)
        )
        private val pageTitles = listOf<String>(
                getString(R.string.opened),
                getString(R.string.closed)
        )

        override fun getItem(position: Int) = pages[position]

        override fun getCount() = pages.size

        override fun getPageTitle(position: Int) = pageTitles[position]
    }
}