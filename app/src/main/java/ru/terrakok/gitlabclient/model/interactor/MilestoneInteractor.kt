package ru.terrakok.gitlabclient.model.interactor

import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDate
import ru.terrakok.gitlabclient.entity.Issue
import ru.terrakok.gitlabclient.entity.MergeRequest
import ru.terrakok.gitlabclient.entity.Milestone
import ru.terrakok.gitlabclient.entity.MilestoneState
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges

class MilestoneInteractor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val defaultPageSize: Int
) {

    val milestoneChanges: Flow<Long> = serverChanges.milestoneChanges

    suspend fun getMilestones(
        projectId: Long,
        state: MilestoneState? = null,
        page: Int,
        pageSize: Int = defaultPageSize
    ): List<Milestone> = api.getMilestones(projectId, state, page, pageSize)

    suspend fun getMilestone(
        projectId: Long,
        milestoneId: Long
    ): Milestone = api.getMilestone(projectId, milestoneId)

    suspend fun createMilestone(
        projectId: Long,
        title: String,
        description: String? = null,
        dueDate: LocalDate? = null,
        startDate: LocalDate? = null
    ): Milestone = api.createMilestone(projectId, title, description, dueDate, startDate)

    suspend fun updateMilestone(
        projectId: Long,
        milestoneId: Long,
        title: String? = null,
        description: String? = null,
        dueDate: LocalDate? = null,
        startDate: LocalDate? = null
    ): Milestone =
        api.updateMilestone(
            projectId, milestoneId, title,
            description, dueDate, startDate
        )

    suspend fun deleteMilestone(
        projectId: Long,
        milestoneId: Long
    ) {
        api.deleteMilestone(projectId, milestoneId)
    }

    suspend fun getMilestoneIssues(
        projectId: Long,
        milestoneId: Long,
        page: Int,
        pageSize: Int = defaultPageSize
    ): List<Issue> =
        api.getMilestoneIssues(projectId, milestoneId, page, pageSize)

    suspend fun getMilestoneMergeRequests(
        projectId: Long,
        milestoneId: Long,
        page: Int,
        pageSize: Int = defaultPageSize
    ): List<MergeRequest> =
        api.getMilestoneMergeRequests(projectId, milestoneId, page, pageSize)
}
