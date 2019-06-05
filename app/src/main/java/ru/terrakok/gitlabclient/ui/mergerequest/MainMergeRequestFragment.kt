package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_main_mr.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.entity.app.target.TargetAction
import ru.terrakok.gitlabclient.presentation.mergerequest.MergeRequestPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.MergeRequestView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.util.addSystemTopPadding
import ru.terrakok.gitlabclient.util.showSnackMessage

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 14.02.18.
 */
class MainMergeRequestFragment : BaseFragment(), MergeRequestView {

    override val layoutRes = R.layout.fragment_main_mr

    @InjectPresenter
    lateinit var presenter: MergeRequestPresenter

    @ProvidePresenter
    fun providePresenter(): MergeRequestPresenter =
        scope.getInstance(MergeRequestPresenter::class.java)

    private val adapter by lazy { MergeRequestPagesAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }
        toolbar.addSystemTopPadding()
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

    private inner class MergeRequestPagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        override fun getItem(position: Int): BaseFragment = when (position) {
            TAB_DETAILS -> Screens.MergeRequestDetails.fragment
            TAB_INFO -> Screens.MergeRequestInfo.fragment
            TAB_COMMITS -> Screens.MergeRequestCommits.fragment
            TAB_NOTES -> Screens.MergeRequestNotes.fragment
            else -> Screens.MergeRequestDiffDataList.fragment
        }

        override fun getCount() = 5

        override fun getPageTitle(position: Int) = when (position) {
            TAB_DETAILS -> getString(R.string.merge_request_details_tab)
            TAB_INFO -> getString(R.string.merge_request_info_tab)
            TAB_COMMITS -> getString(R.string.merge_request_commits_tab)
            TAB_NOTES -> getString(R.string.merge_request_discussion_tab)
            TAB_DIFF_DATA -> getString(R.string.merge_request_changes_tab)
            else -> null
        }
    }

    companion object {
        private const val TAB_DETAILS = 0
        private const val TAB_INFO = 1
        private const val TAB_COMMITS = 2
        private const val TAB_NOTES = 3
        private const val TAB_DIFF_DATA = 4
    }
}