package ru.terrakok.gitlabclient.ui.issue

import android.os.Bundle
import android.support.v4.app.FragmentPagerAdapter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_mr.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.presentation.issue.IssuePresenter
import ru.terrakok.gitlabclient.presentation.issue.IssueView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 14.02.18.
 */
class IssueFragment : BaseFragment(), IssueView {

    override val layoutRes = R.layout.fragment_issue

    @InjectPresenter
    lateinit var presenter: IssuePresenter

    @ProvidePresenter
    fun providePresenter() =
        Toothpick
            .openScope(DI.ISSUE_FLOW_SCOPE)
            .getInstance(IssuePresenter::class.java)

    private val adapter by lazy { IssuePagesAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        viewPager.adapter = adapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        presenter.onBackPressed()
    }

    override fun setTitle(title: String, subtitle: String) {
        toolbar.title = title
        toolbar.subtitle = subtitle
    }

    override fun showBlockingProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    private inner class IssuePagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        override fun getItem(position: Int): BaseFragment? = when (position) {
            0 -> Screens.IssueInfo.fragment
            1 -> Screens.IssueNotes.fragment
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