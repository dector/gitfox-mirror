package ru.terrakok.gitlabclient.ui.mergerequest

import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_mr_info.*
import ru.noties.markwon.Markwon
import ru.terrakok.gitlabclient.R
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

    override fun showInfo(mrInfo: MergeRequestInfoView.MergeRequestInfo) {
        val mergeRequest = mrInfo.mr

        (parentFragment as? ToolbarConfigurator)
            ?.setTitle("!${mergeRequest.iid}", mrInfo.project.name)

        titleTextView.text = mergeRequest.title
        when (mergeRequest.state) {
            MergeRequestState.OPENED -> {
                stateImageView.tint(R.color.green)
                subtitleTextView.text = String.format(
                    getString(R.string.merge_request_info_subtitle),
                    getString(R.string.target_status_opened),
                    mergeRequest.author.name,
                    mergeRequest.createdAt.humanTime(resources)
                )
            }
            MergeRequestState.MERGED -> {
                stateImageView.tint(R.color.blue)
                subtitleTextView.text =
                    if (mergeRequest.mergedBy != null && mergeRequest.mergedAt != null) {
                        String.format(
                            getString(R.string.issue_info_subtitle),
                            getString(R.string.target_status_merged),
                            mergeRequest.mergedBy.name,
                            mergeRequest.mergedAt.humanTime(resources)
                        )
                    } else {
                        getString(R.string.target_status_merged)
                    }
            }
            MergeRequestState.CLOSED -> {
                stateImageView.tint(R.color.red)
                subtitleTextView.text =
                    if (mergeRequest.closedBy != null && mergeRequest.closedAt != null) {
                        String.format(
                            getString(R.string.issue_info_subtitle),
                            getString(R.string.target_status_closed),
                            mergeRequest.closedBy.name,
                            mergeRequest.closedAt.humanTime(resources)
                        )
                    } else {
                        getString(R.string.target_status_closed)
                    }
            }
        }
        avatarImageView.loadRoundedImage(mergeRequest.author.avatarUrl, context)
        Markwon.setText(descriptionTextView, mrInfo.mdDescription)
    }

    override fun showProgress(show: Boolean) {
        view?.visible(!show)
        showProgressDialog(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    interface ToolbarConfigurator {
        fun setTitle(title: String, subTitle: String)
    }
}