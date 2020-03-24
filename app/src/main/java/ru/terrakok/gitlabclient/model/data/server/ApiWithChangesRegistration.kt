package ru.terrakok.gitlabclient.model.data.server

import ru.terrakok.gitlabclient.entity.*
import ru.terrakok.gitlabclient.model.data.state.ServerChanges

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 14.10.18.
 */
class ApiWithChangesRegistration(
    private val serverApi: GitlabApi,
    private val serverChanges: ServerChanges
) : GitlabApi by serverApi {

    override suspend fun createMilestone(
        projectId: Long,
        title: String,
        description: String?,
        dueDate: Date?,
        startDate: Date?
    ): Milestone =
        serverApi.createMilestone(projectId, title, description, dueDate, startDate)
            .also { serverChanges.milestoneChanged(it.id) }

    override suspend fun updateMilestone(
        projectId: Long,
        mileStoneId: Long,
        title: String?,
        description: String?,
        dueDate: Date?,
        startDate: Date?
    ): Milestone =
        serverApi.updateMilestone(projectId, mileStoneId, title, description, dueDate, startDate)
            .also { serverChanges.milestoneChanged(mileStoneId) }

    override suspend fun deleteMilestone(projectId: Long, mileStoneId: Long) {
        serverApi.deleteMilestone(projectId, mileStoneId)
        serverChanges.milestoneChanged(mileStoneId)
    }

    override suspend fun createLabel(
        projectId: Long,
        name: String,
        color: String,
        description: String?,
        priority: Int?
    ): Label =
        serverApi.createLabel(projectId, name, color, description, priority)
            .also { serverChanges.labelChanged(it.id) }

    override suspend fun deleteLabel(projectId: Long, name: String) {
        serverApi.deleteLabel(projectId, name)
        serverChanges.labelChanged()
    }

    override suspend fun subscribeToLabel(projectId: Long, labelId: Long): Label =
        serverApi.subscribeToLabel(projectId, labelId)
            .also { serverChanges.labelChanged(labelId) }

    override suspend fun unsubscribeFromLabel(projectId: Long, labelId: Long): Label =
        serverApi.unsubscribeFromLabel(projectId, labelId)
            .also { serverChanges.labelChanged(labelId) }

    override suspend fun addMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String?
    ) {
        serverApi.addMember(projectId, userId, accessLevel, expiresDate)
        serverChanges.memberChanged(userId)
    }

    override suspend fun editMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String?
    ) {
        serverApi.editMember(projectId, userId, accessLevel, expiresDate)
        serverChanges.memberChanged(userId)
    }

    override suspend fun deleteMember(projectId: Long, userId: Long) {
        serverApi.deleteMember(projectId, userId)
        serverChanges.memberChanged(userId)
    }

    override suspend fun markPendingTodoAsDone(id: Long): Todo =
        serverApi.markPendingTodoAsDone(id)
            .also { serverChanges.todoChanged(id) }

    override suspend fun markAllPendingTodosAsDone() {
        serverApi.markAllPendingTodosAsDone()
        serverChanges.todoChanged()
    }

    override suspend fun editIssue(
        projectId: Long,
        issueId: Long,
        stateEvent: IssueStateEvent
    ) {
        serverApi.editIssue(projectId, issueId, stateEvent)
        serverChanges.issueChanged(issueId)
    }
}
