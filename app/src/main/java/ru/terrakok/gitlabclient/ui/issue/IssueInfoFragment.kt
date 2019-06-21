package ru.terrakok.gitlabclient.ui.issue

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_issue_info.*
import kotlinx.android.synthetic.main.item_target_badge.view.*
import org.threeten.bp.LocalDate
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.entity.ShortUser
import ru.terrakok.gitlabclient.entity.TimeStats
import ru.terrakok.gitlabclient.entity.issue.Issue
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.extension.*
import ru.terrakok.gitlabclient.presentation.issue.info.IssueInfoPresenter
import ru.terrakok.gitlabclient.presentation.issue.info.IssueInfoView
import ru.terrakok.gitlabclient.ui.global.BaseFragment
import ru.terrakok.gitlabclient.ui.global.list.AssigneesAdapter

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

    private val assigneesAdapter by lazy { AssigneesAdapter() }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(assigneesList) {
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = assigneesAdapter
        }
    }

    override fun showInfo(issue: Issue) {
        with(issue) {
            showAssignees(assignees ?: assignee?.let { listOf(it) } ?: emptyList())
            showMilestone(milestone)
            showDueDate(dueDate)
            showTimeStats(timeStats)
            showWeight(weight)
            showLockIssue(discussionLocked)
            showConfidentiality(confidential)
            showLabels(labels)
        }
    }

    private fun showAssignees(assignees: List<ShortUser>) {
        if (assignees.isNotEmpty()) {
            assigneesNone.visible(false)
            assigneesAdapter.setData(assignees)
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

    private fun showDueDate(dueDate: LocalDate?) {
        dueDateValue.text = if (dueDate != null) {
            dueDate.humanDate()
        } else {
            getString(R.string.issue_merge_request_none)
        }
        dueDateValue.alpha = if (dueDate != null) ALPHA_VALUE else ALPHA_NONE
    }

    private fun showTimeStats(timeStats: TimeStats) {
        timeStatsValue.setTimeStats(timeStats)
    }

    private fun showWeight(weight: Int?) {
        weightValue.text = weight?.toString() ?: getString(R.string.issue_merge_request_none)
        weightValue.alpha = if (weight != null) ALPHA_VALUE else ALPHA_NONE
    }

    private fun showLockIssue(discussionLocked: Boolean) {
        val stringRes = if (discussionLocked) R.string.lock_locked else R.string.lock_unlocked
        lockIssueValue.text = getString(stringRes)
        lockIssueValue.alpha = if (discussionLocked) ALPHA_VALUE else ALPHA_NONE
    }

    private fun showConfidentiality(confidential: Boolean) {
        val stringRes = if (confidential) {
            R.string.confidentiality_confidential
        } else {
            R.string.confidentiality_not_confidential
        }
        confidentialityValue.text = getString(stringRes)
        confidentialityValue.alpha = if (confidential) ALPHA_VALUE else ALPHA_NONE
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
        issueInfoContainer.visible(!show)
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