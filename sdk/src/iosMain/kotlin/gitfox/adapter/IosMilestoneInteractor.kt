package gitfox.adapter

import gitfox.entity.*
import gitfox.model.interactor.MilestoneInteractor
import kotlinx.coroutines.CoroutineScope

class IosMilestoneInteractor internal constructor(
    private val interactor: MilestoneInteractor,
    private val defaultPageSize: Int
) : CoroutineScope by CoroutineScope(MainLoopDispatcher) {
    val milestoneChanges = interactor.milestoneChanges.wrap()

    fun getMilestones(
        projectId: Long,
        state: MilestoneState? = null,
        page: Int,
        pageSize: Int = defaultPageSize,
        callback: (result: List<Milestone>?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.getMilestones(projectId, state, page, pageSize) }
    }

    fun getMilestone(
        projectId: Long,
        milestoneId: Long,
        callback: (result: Milestone?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.getMilestone(projectId, milestoneId) }
    }

    fun createMilestone(
        projectId: Long,
        title: String,
        description: String? = null,
        dueDate: Date? = null,
        startDate: Date? = null,
        callback: (result: Milestone?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.createMilestone(projectId, title, description, dueDate, startDate) }
    }

    fun updateMilestone(
        projectId: Long,
        milestoneId: Long,
        title: String? = null,
        description: String? = null,
        dueDate: Date? = null,
        startDate: Date? = null,
        callback: (result: Milestone?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.updateMilestone(projectId, milestoneId, title, description, dueDate, startDate) }
    }

    fun deleteMilestone(
        projectId: Long,
        milestoneId: Long,
        callback: (result: Unit?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.deleteMilestone(projectId, milestoneId) }
    }

    fun getMilestoneIssues(
        projectId: Long,
        milestoneId: Long,
        page: Int,
        pageSize: Int = defaultPageSize,
        callback: (result: List<Issue>?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.getMilestoneIssues(projectId, milestoneId, page, pageSize) }
    }

    fun getMilestoneMergeRequests(
        projectId: Long,
        milestoneId: Long,
        page: Int,
        pageSize: Int = defaultPageSize,
        callback: (result: List<MergeRequest>?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.getMilestoneMergeRequests(projectId, milestoneId, page, pageSize) }
    }
}