package gitfox.model.interactor

import gitfox.entity.*
import gitfox.model.data.cache.ProjectMilestoneCache
import gitfox.model.data.server.GitlabApi
import gitfox.model.data.state.ServerChanges
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.toList

class MilestoneInteractor internal constructor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val defaultPageSize: Int,
    private val projectMilestoneCache: ProjectMilestoneCache
) {

    val milestoneChanges: Flow<Long> = serverChanges.milestoneChanges

    suspend fun getMilestones(
        projectId: Long,
        state: MilestoneState? = null,
        page: Int,
        pageSize: Int = defaultPageSize
    ): List<Milestone> = api.getMilestones(projectId, state, page, pageSize)

    suspend fun getAllProjectMilestones(projectId: Long): List<Milestone> {
        return projectMilestoneCache.getOrPut(projectId) {
            this.getAllProjectMilestonesFromServer(projectId)
        }
    }

    private suspend fun getAllProjectMilestonesFromServer(
        projectId: Long,
        state: MilestoneState? = null
    ): List<Milestone> =
        (0..Int.MAX_VALUE)
            .asFlow()
            .map { page -> api.getMilestones(projectId, state, page, defaultPageSize) }
            .takeWhile { it.isNotEmpty() }
            .flatMapConcat { it.asFlow() }
            .toList()

    suspend fun getMilestone(
        projectId: Long,
        milestoneId: Long
    ): Milestone = api.getMilestone(projectId, milestoneId)

    suspend fun createMilestone(
        projectId: Long,
        title: String,
        description: String? = null,
        dueDate: Date? = null,
        startDate: Date? = null
    ): Milestone = api.createMilestone(projectId, title, description, dueDate, startDate)

    suspend fun updateMilestone(
        projectId: Long,
        milestoneId: Long,
        title: String? = null,
        description: String? = null,
        dueDate: Date? = null,
        startDate: Date? = null
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
