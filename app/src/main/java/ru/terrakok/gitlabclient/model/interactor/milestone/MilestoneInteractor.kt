package ru.terrakok.gitlabclient.model.interactor.milestone

import ru.terrakok.gitlabclient.entity.milestone.MilestoneState
import ru.terrakok.gitlabclient.model.repository.issue.IssueRepository
import ru.terrakok.gitlabclient.model.repository.mergerequest.MergeRequestRepository
import ru.terrakok.gitlabclient.model.repository.milestone.MilestoneRepository
import javax.inject.Inject

class MilestoneInteractor @Inject constructor(
    private val milestoneRepository: MilestoneRepository,
    private val mergeRequestRepository: MergeRequestRepository,
    private val issueRepository: IssueRepository
) {

    fun getMilestones(
        projectId: Long,
        milestoneState: MilestoneState
    ) = milestoneRepository
        .getMilestones(
            projectId = projectId,
            state = milestoneState
        )

    fun getMilestone(
        projectId: Long,
        milestoneId: Long
    ) = milestoneRepository
        .getMilestone(
            projectId = projectId,
            milestoneId = milestoneId
        )

    fun createMilestone(
        projectId: Long,
        title: String,
        description: String?,
        dueDate: String?,
        startDate: String?
    ) = milestoneRepository
        .createMilestone(
            projectId = projectId,
            title = title,
            description = description,
            dueDate = dueDate,
            startDate = startDate
        )

    fun updateMilestone(
        projectId: Long,
        milestoneId: Long,
        title: String?,
        description: String?,
        dueDate: String?,
        startDate: String?
    ) = milestoneRepository
        .updateMilestone(
            projectId = projectId,
            milestoneId = milestoneId,
            title = title,
            description = description,
            dueDate = dueDate,
            startDate = startDate
        )

    fun deleteMilestone(
        projectId: Long,
        milestoneId: Long
    ) = milestoneRepository.deleteMilestone(projectId, milestoneId)

    fun getAssignedIssues(
        projectId: Long,
        milestoneId: Long
    ) = issueRepository.getMilestoneIssues(projectId, milestoneId)

    fun getAssignedMergeRequests(
        projectId: Long,
        milestoneId: Long
    ) = mergeRequestRepository.getMilestoneMergeRequests(projectId, milestoneId)
}