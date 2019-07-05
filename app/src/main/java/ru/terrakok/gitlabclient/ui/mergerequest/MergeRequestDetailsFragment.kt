package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_mr_details.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.extension.humanTime
import ru.terrakok.gitlabclient.extension.showSnackMessage
import ru.terrakok.gitlabclient.extension.tint
import ru.terrakok.gitlabclient.extension.visible
import ru.terrakok.gitlabclient.presentation.mergerequest.details.MergeRequestDetailsPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.details.MergeRequestDetailsView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.view.custom.bindShortUser

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
        descriptionTextView.initWithParentDelegate(mvpDelegate)
    }

    override fun showDetails(mr: MergeRequest) {
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
        descriptionTextView.setMarkdown(mr.description, mr.projectId)
    }

    override fun showEmptyProgress(show: Boolean) {
        mrDetailsContainer.visible(!show)
        fullscreenProgressView.visible(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}