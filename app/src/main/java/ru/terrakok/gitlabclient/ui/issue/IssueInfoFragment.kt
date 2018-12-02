package ru.terrakok.gitlabclient.ui.issue

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_issue_info.*
import ru.noties.markwon.Markwon
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.issue.Issue
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.extension.*
import ru.terrakok.gitlabclient.presentation.issue.info.IssueInfoPresenter
import ru.terrakok.gitlabclient.presentation.issue.info.IssueInfoView
import ru.terrakok.gitlabclient.ui.global.BaseFragment

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.02.18.
 */
class IssueInfoFragment : BaseFragment(), IssueInfoView {

    override val layoutRes = R.layout.fragment_issue_info

    @InjectPresenter
    lateinit var presenter: IssueInfoPresenter

    @ProvidePresenter
    fun providePresenter(): IssueInfoPresenter =
        scope.getInstance(IssueInfoPresenter::class.java)

    override fun showInfo(issue: Issue, mdDescription: CharSequence) {
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
        avatarImageView.loadRoundedImage(issue.author.avatarUrl, context)
        Markwon.setText(descriptionTextView, mdDescription)
    }

    override fun showEmptyProgress(show: Boolean) {
        issueInfoContainer.visible(!show)
        fullscreenProgressView.visible(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}