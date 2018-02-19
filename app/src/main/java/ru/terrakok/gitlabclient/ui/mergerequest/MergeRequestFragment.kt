package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_mr.*
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 14.02.18.
 */
class MergeRequestFragment : BaseFragment(), MergeRequestInfoFragment.ToolbarConfigurator {

    override val layoutRes = R.layout.fragment_mr

    @Inject
    lateinit var router: Router

    private val adapter by lazy { MergeRequestPagesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.inject(this, Toothpick.openScope(DI.MERGE_REQUEST_SCOPE))
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        viewPager.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        router.exit()
    }

    override fun setTitle(title: String, subTitle: String) {
        toolbar.title = title
        toolbar.subtitle = subTitle
    }

    private inner class MergeRequestPagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        override fun getItem(position: Int): BaseFragment? = when (position) {
            0 -> MergeRequestInfoFragment()
            1 -> MergeRequestNotesFragment()
            else -> null
        }

        override fun getCount() = 2

        override fun getPageTitle(position: Int) = when (position) {
            0 -> getString(R.string.merge_request_info_tab)
            1 -> getString(R.string.merge_request_discussion_tab)
            else -> null
        }
    }
}