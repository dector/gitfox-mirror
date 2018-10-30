package ru.terrakok.gitlabclient.ui.mergerequest

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_mr_info.*
import ru.noties.markwon.Markwon
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.extension.*
import ru.terrakok.gitlabclient.presentation.mergerequest.info.MergeRequestInfoPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.info.MergeRequestInfoView
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import toothpick.Toothpick

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.02.18.
 */
class MergeRequestInfoFragment : BaseFragment(), MergeRequestInfoView {

    override val layoutRes = R.layout.fragment_mr_info

    @InjectPresenter
    lateinit var presenter: MergeRequestInfoPresenter

    @ProvidePresenter
    fun providePresenter() =
        Toothpick.openScope(DI.MERGE_REQUEST_FLOW_SCOPE)
            .getInstance(MergeRequestInfoPresenter::class.java)

    override fun showInfo(mr: MergeRequest, mdDescription: CharSequence) {
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
        avatarImageView.loadRoundedImage(mr.author.avatarUrl, context)
        Markwon.setText(descriptionTextView, mdDescription)
    }

    override fun showEmptyProgress(show: Boolean) {
        mrInfoContainer.visible(!show)
        fullscreenProgressView.visible(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }
}