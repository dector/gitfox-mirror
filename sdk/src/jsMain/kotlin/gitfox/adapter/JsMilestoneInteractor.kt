package gitfox.adapter

import gitfox.entity.Date
import gitfox.entity.MilestoneState
import gitfox.model.interactor.MilestoneInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

class JsMilestoneInteractor internal constructor(
    private val interactor: MilestoneInteractor,
    private val defaultPageSize: Int
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {
    val milestoneChanges = interactor.milestoneChanges.wrap()

    fun getMilestones(
        projectId: Long,
        state: MilestoneState? = null,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = promise {
        interactor.getMilestones(projectId, state, page, pageSize)
    }

    fun getMilestone(
        projectId: Long,
        milestoneId: Long
    ) = promise {
        interactor.getMilestone(projectId, milestoneId)
    }

    fun createMilestone(
        projectId: Long,
        title: String,
        description: String? = null,
        dueDate: Date? = null,
        startDate: Date? = null
    ) = promise {
        interactor.createMilestone(projectId, title, description, dueDate, startDate)
    }

    fun updateMilestone(
        projectId: Long,
        milestoneId: Long,
        title: String? = null,
        description: String? = null,
        dueDate: Date? = null,
        startDate: Date? = null
    ) = promise {
        interactor.updateMilestone(projectId, milestoneId, title, description, dueDate, startDate)
    }

    fun deleteMilestone(
        projectId: Long,
        milestoneId: Long
    ) = promise {
        interactor.deleteMilestone(projectId, milestoneId)
    }

    fun getMilestoneIssues(
        projectId: Long,
        milestoneId: Long,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = promise {
        interactor.getMilestoneIssues(projectId, milestoneId, page, pageSize)
    }

    fun getMilestoneMergeRequests(
        projectId: Long,
        milestoneId: Long,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = promise {
        interactor.getMilestoneMergeRequests(projectId, milestoneId, page, pageSize)
    }
}