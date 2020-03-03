package ru.terrakok.gitlabclient.ui.issue

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_issue_details.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.issue.Issue
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.presentation.issue.details.IssueDetailsPresenter
import ru.terrakok.gitlabclient.presentation.issue.details.IssueDetailsView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.view.custom.bindShortUser
import ru.terrakok.gitlabclient.util.*

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 26.05.19.
 */
class IssueDetailsFragment : BaseFragment(), IssueDetailsView {

    override val layoutRes = R.layout.fragment_issue_details

    @InjectPresenter
    lateinit var presenter: IssueDetailsPresenter

    @ProvidePresenter
    fun providePresenter(): IssueDetailsPresenter =
        scope.getInstance(IssueDetailsPresenter::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        descriptionTextView.initWithParentDelegate(mvpDelegate)
        issueDetailsContainer.addSystemBottomPadding()
    }

    override fun showDetails(issue: Issue) {
        titleTextView.text = issue.title
        stateImageView.setImageResource(R.drawable.circle)
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
                subtitleTextView.text = if (issue.closedBy != null && issue.closedAt != null) {
                    String.format(
                        getString(R.string.issue_info_subtitle),
                        getString(R.string.target_status_closed),
                        issue.closedBy.name,
                        issue.closedAt.humanTime(resources)
                    )
                } else {
                    getString(R.string.target_status_closed)
                }
            }
        }
        avatarImageView.bindShortUser(issue.author)
        descriptionTextView.setMarkdown(issue.description, issue.projectId)
    }

    override fun showEmptyProgress(show: Boolean) {
        issueDetailsContainer.visible(!show)
        fullscreenProgressView.visible(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
