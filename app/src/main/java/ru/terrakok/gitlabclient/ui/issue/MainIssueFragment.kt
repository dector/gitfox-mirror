package ru.terrakok.gitlabclient.ui.issue

import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_main_mr.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.app.target.TargetAction
import ru.terrakok.gitlabclient.presentation.issue.IssuePresenter
import ru.terrakok.gitlabclient.presentation.issue.IssueView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.util.showSnackMessage

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 14.02.18.
 */
class MainIssueFragment : BaseFragment(), IssueView {

    override val layoutRes = R.layout.fragment_main_issue

    @InjectPresenter
    lateinit var presenter: IssuePresenter

    @ProvidePresenter
    fun providePresenter(): IssuePresenter =
        scope.getInstance(IssuePresenter::class.java)

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

    override fun selectActionTab(targetAction: TargetAction) {
        when (targetAction) {
            is TargetAction.CommentedOn -> {
                viewPager.currentItem = TAB_NOTES
            }
        }
    }

    private inner class IssuePagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        override fun getItem(position: Int): BaseFragment = when (position) {
            TAB_DETAILS -> Screens.IssueDetails.fragment
            TAB_INFO -> Screens.IssueInfo.fragment
            else -> Screens.IssueNotes.fragment
        }

        override fun getCount() = 3

        override fun getPageTitle(position: Int) = when (position) {
            TAB_DETAILS -> getString(R.string.merge_request_details_tab)
            TAB_INFO -> getString(R.string.merge_request_info_tab)
            TAB_NOTES -> getString(R.string.merge_request_discussion_tab)
            else -> null
        }
    }

    companion object {
        private const val TAB_DETAILS = 0
        private const val TAB_INFO = 1
        private const val TAB_NOTES = 2
    }
}