package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_mr_details.*
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import ru.noties.markwon.Markwon
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.MergeRequest
import ru.terrakok.gitlabclient.entity.MergeRequestState
import ru.terrakok.gitlabclient.presentation.mergerequest.details.MergeRequestDetailsPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.details.MergeRequestDetailsView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.view.custom.bindShortUser
import ru.terrakok.gitlabclient.util.*

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 31.05.19.
 */
class MergeRequestDetailsFragment : BaseFragment(), MergeRequestDetailsView {

    override val layoutRes = R.layout.fragment_mr_details

    @InjectPresenter
    lateinit var presenter: MergeRequestDetailsPresenter

    @ProvidePresenter
    fun providePresenter() =
        scope.getInstance(MergeRequestDetailsPresenter::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mrDetailsContainer.addSystemBottomPadding()
    }

    override fun showDetails(mr: MergeRequest, mdDescription: CharSequence) {
        titleTextView.text = mr.title
        when (mr.state) {
            MergeRequestState.OPENED -> {
                stateImageView.tint(R.color.green)
                subtitleTextView.text = String.format(
                    getString(R.string.merge_request_info_subtitle),
                    getString(R.string.target_status_opened),
                    mr.author.name,
                    mr.createdAt.humanTime(resources)
                )
            }
            MergeRequestState.MERGED -> {
                stateImageView.tint(R.color.blue)
                subtitleTextView.text =
                    if (mr.mergedBy != null && mr.mergedAt != null) {
                        String.format(
                            getString(R.string.issue_info_subtitle),
                            getString(R.string.target_status_merged),
                            mr.mergedBy.name,
                            mr.mergedAt.humanTime(resources)
                        )
                    } else {
                        getString(R.string.target_status_merged)
                    }
            }
            MergeRequestState.CLOSED -> {
                stateImageView.tint(R.color.red)
                subtitleTextView.text =
                    if (mr.closedBy != null && mr.closedAt != null) {
                        String.format(
                            getString(R.string.issue_info_subtitle),
                            getString(R.string.target_status_closed),
                            mr.closedBy.name,
                            mr.closedAt.humanTime(resources)
                        )
                    } else {
                        getString(R.string.target_status_closed)
                    }
            }
        }
        avatarImageView.bindShortUser(mr.author)
        Markwon.setText(descriptionTextView, mdDescription)
    }

    override fun showEmptyProgress(show: Boolean) {
        mrDetailsContainer.visible(!show)
        fullscreenProgressView.visible(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}
