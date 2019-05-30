package ru.terrakok.gitlabclient.ui.mergerequest

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_mr_info.*
import kotlinx.android.synthetic.main.item_target_badge.view.*
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.ShortUser
import ru.terrakok.gitlabclient.entity.TimeStats
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestMergeStatus
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.extension.*
import ru.terrakok.gitlabclient.presentation.mergerequest.info.MergeRequestInfoPresenter
import ru.terrakok.gitlabclient.presentation.mergerequest.info.MergeRequestInfoView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.AssigneesAdapter

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.02.18.
 */
class MergeRequestInfoFragment : BaseFragment(), MergeRequestInfoView {

    override val layoutRes = R.layout.fragment_mr_info

    @InjectPresenter
    lateinit var presenter: MergeRequestInfoPresenter

    @ProvidePresenter
    fun providePresenter() =
        scope.getInstance(MergeRequestInfoPresenter::class.java)

    override fun showInfo(mr: MergeRequest) {
        with(mr) {
            showAssignees(assignees)
            showMilestone(milestone)
            showMergeStatus(state, mergeStatus)
            showTimeStats(timeStats)
            showLockMergeRequest(discussionLocked)
            showLabels(labels)
        }
    }

    private fun showAssignees(assignees: List<ShortUser>) {
        if (assignees.isNotEmpty()) {
            assigneesNone.visible(false)
            with(assigneesList) {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                setHasFixedSize(true)
                adapter = AssigneesAdapter { presenter.onAssigneeClicked(it) }.apply {
                    setData(assignees)
                }
            }
        } else {
            assigneesList.visible(false)
        }
    }

    private fun showMilestone(milestone: Milestone?) {
        milestoneValue.text = if (milestone != null) {
            milestone.title
        } else {
            getString(R.string.issue_merge_request_none)
        }
        milestoneValue.alpha = if (milestone != null) ALPHA_VALUE else ALPHA_NONE
    }

    private fun showMergeStatus(state: MergeRequestState, mergeStatus: MergeRequestMergeStatus) {
        mergeStatusValue.text = if (state == MergeRequestState.OPENED) {
            mergeStatus.getHumanName(resources)
        } else {
            getString(R.string.issue_merge_request_none)
        }
        mergeStatusValue.alpha = if (state == MergeRequestState.OPENED) ALPHA_VALUE else ALPHA_NONE
    }

    private fun showTimeStats(timeStats: TimeStats) {
        timeStatsValue.setTimeStats(timeStats)
    }

    private fun showLockMergeRequest(discussionLocked: Boolean) {
        val stringRes = if (discussionLocked) R.string.lock_locked else R.string.lock_unlocked
        lockMergeRequestValue.text = getString(stringRes)
        lockMergeRequestValue.alpha = if (discussionLocked) ALPHA_VALUE else ALPHA_NONE
    }

    private fun showLabels(labels: List<String>) {
        if (labels.isNotEmpty()) {
            labelsNone.visible(false)
            val colorPrimary = requireContext().color(R.color.colorPrimary)
            val colorPrimaryLight = requireContext().color(R.color.colorPrimaryLight)
            (1..labels.size).forEach { _ ->
                labelsValue.inflate(R.layout.item_target_badge, true)
            }
            labels.forEachIndexed { index, label ->
                val labelView = labelsValue.getChildAt(index)
                labelView.textTextView.text = label
                labelView.textTextView.setTextColor(colorPrimary)
                labelView.textTextView.setBackgroundColor(colorPrimaryLight)
            }
        } else {
            labelsValue.visible(false)
        }
    }

    override fun showEmptyProgress(show: Boolean) {
        mrInfoContainer.visible(!show)
        fullscreenProgressView.visible(show)
    }

    override fun showMessage(message: String) {
        showSnackMessage(message)
    }

    companion object {
        private const val ALPHA_VALUE = 1f
        private const val ALPHA_NONE = 0.38f
    }
}