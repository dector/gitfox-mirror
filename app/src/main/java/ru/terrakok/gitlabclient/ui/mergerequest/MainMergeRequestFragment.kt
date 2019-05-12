package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_main_mr.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.presentation.mergerequest.MergeRequestPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.MergeRequestView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

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

    private inner class MergeRequestPagesAdapter : FragmentPagerAdapter(childFragmentManager) {
        override fun getItem(position: Int): BaseFragment = when (position) {
            0 -> Screens.MergeRequestInfo.fragment
            1 -> Screens.MergeRequestCommits.fragment
            2 -> Screens.MergeRequestNotes.fragment
            else -> Screens.MergeRequestChanges.fragment
        }

        override fun getCount() = 4

        override fun getPageTitle(position: Int) = when (position) {
            0 -> getString(R.string.merge_request_info_tab)
            1 -> getString(R.string.merge_request_commits_tab)
            2 -> getString(R.string.merge_request_discussion_tab)
            3 -> getString(R.string.merge_request_changes_tab)
            else -> null
        }
    }
}