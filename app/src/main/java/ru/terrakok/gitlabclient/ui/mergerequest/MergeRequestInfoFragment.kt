package ru.terrakok.gitlabclient.ui.mergerequest

import android.os.Bundle
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_mr_info.*
import ru.noties.markwon.Markwon
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.extension.color
import ru.terrakok.gitlabclient.extension.humanTime
import ru.terrakok.gitlabclient.extension.loadRoundedImage
import ru.terrakok.gitlabclient.presentation.mergerequest.MergeRequestInfoPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.MergeRequestInfoView
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
            Toothpick.openScope(DI.MERGE_REQUEST_SCOPE)
                    .getInstance(MergeRequestInfoPresenter::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar.setNavigationOnClickListener { presenter.onBackPressed() }
    }

    override fun showMergeRequest(mrInfo: MergeRequestInfoView.MergeRequestInfo) {
        val mergeRequest = mrInfo.mr
        val project = mrInfo.project

        toolbar.title = "!${mergeRequest.iid}"
        toolbar.subtitle = project.name
        titleTextView.text = mergeRequest.title
        stateImageView.setImageResource(R.drawable.circle)
        // TODO: merge request info (Display action user name for the MERGED/CLOSED states).
        // Wait for https://gitlab.com/gitlab-org/gitlab-ce/issues/41905.
        when (mergeRequest.state) {
            MergeRequestState.OPENED -> {
                stateImageView.setColorFilter(context!!.color(R.color.green))
                subtitleTextView.text = String.format(
                        getString(R.string.merge_request_info_subtitle),
                        getString(R.string.target_status_opened),
                        mergeRequest.author.name,
                        mergeRequest.createdAt.humanTime(resources)
                )
            }
            MergeRequestState.MERGED -> {
                stateImageView.setColorFilter(context!!.color(R.color.blue))
                subtitleTextView.text = getString(R.string.target_status_merged)
            }
            MergeRequestState.CLOSED -> {
                stateImageView.setColorFilter(context!!.color(R.color.red))
                subtitleTextView.text = getString(R.string.target_status_closed)
            }
        }
        avatarImageView.loadRoundedImage(mergeRequest.author.avatarUrl, context)
        Markwon.setText(descriptionTextView, mrInfo.mdDescription)
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