package ru.terrakok.gitlabclient.ui.issue

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_issue_info.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.extension.humanTime
import ru.terrakok.gitlabclient.extension.loadRoundedImage
import ru.terrakok.gitlabclient.presentation.issue.IssueInfoPresenter
import ru.terrakok.gitlabclient.presentation.issue.IssueInfoView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.02.18.
 */
class IssueInfoFragment : BaseFragment(), IssueInfoView {

    override val layoutRes = R.layout.fragment_issue_info

    @InjectPresenter lateinit var presenter: IssueInfoPresenter

    @ProvidePresenter
    fun providePresenter() =
        Toothpick.openScope(DI.ISSUE_SCOPE)
                .getInstance(IssueInfoPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }
    }

    override fun showIssue(issueInfo: IssueInfoView.IssueInfo) {
        val issue = issueInfo.issue
        val project = issueInfo.project

        toolbar.title = "#${issue.iid}"
        toolbar.subtitle = project.name
        titleTextView.text = issue.title
        stateImageView.setImageResource(R.drawable.circle)
        // TODO: issue info (Display action user name for the CLOSED states).
        // Wait for https://gitlab.com/gitlab-org/gitlab-ce/issues/41967
        when (issue.state) {
            IssueState.OPENED -> {
                stateImageView.setColorFilter(context!!.color(R.color.green))
                subtitleTextView.text = String.format(
                        getString(R.string.issue_info_subtitle),
                        getString(R.string.target_status_opened),
                        issue.author.name,
                        issue.createdAt.humanTime(resources)
                )
            }
            IssueState.CLOSED -> {
                stateImageView.setColorFilter(context!!.color(R.color.red))
                subtitleTextView.text = getString(R.string.target_status_closed)
            }
        }
        avatarImageView.loadRoundedImage(issue.author.avatarUrl, context)
        descriptionWebView.loadData(issueInfo.htmlDescription, "text/html", "UTF-8")
    }

    override fun showProgress(show: Boolean) {
        showProgressDialog(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    override fun onBackPressed() {
        presenter.onBackPressed()
    }
}