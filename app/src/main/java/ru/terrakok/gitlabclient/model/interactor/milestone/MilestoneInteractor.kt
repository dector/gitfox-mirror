package ru.terrakok.gitlabclient.model.interactor.milestone

import org.threeten.bp.LocalDate
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

    val milestoneChanges = milestoneRepository.milestoneChanges

    fun getMilestones(
        projectId: Long,
        milestoneState: MilestoneState?,
        page: Int
    ) = milestoneRepository
        .getMilestones(projectId, milestoneState, page)

    fun getMilestone(
        projectId: Long,
        milestoneId: Long
    ) = milestoneRepository
        .getMilestone(projectId, milestoneId)

    fun createMilestone(
        projectId: Long,
        title: String,
        description: String?,
        dueDate: LocalDate?,
        startDate: LocalDate?
    ) = milestoneRepository
        .createMilestone(projectId, title, description, dueDate, startDate)

    fun updateMilestone(
        projectId: Long,
        milestoneId: Long,
        title: String?,
        description: String?,
        dueDate: LocalDate?,
        startDate: LocalDate?
    ) = milestoneRepository
        .updateMilestone(projectId, milestoneId, title, description, dueDate, startDate)

    fun deleteMilestone(
        projectId: Long,
        milestoneId: Long
    ) = milestoneRepository.deleteMilestone(projectId, milestoneId)

    fun getAssignedIssues(
        projectId: Long,
        milestoneId: Long,
        page: Int
    ) = issueRepository.getMilestoneIssues(projectId, milestoneId, page)

    fun getAssignedMergeRequests(
        projectId: Long,
        milestoneId: Long,
        page: Int
    ) = mergeRequestRepository.getMilestoneMergeRequests(projectId, milestoneId, page)
}