package ru.terrakok.gitlabclient.model.data.server

import io.reactivex.Completable
import io.reactivex.Single
import org.threeten.bp.LocalDate
import ru.terrakok.gitlabclient.entity.IssueStateEvent
import ru.terrakok.gitlabclient.entity.Label
import ru.terrakok.gitlabclient.entity.Milestone
import ru.terrakok.gitlabclient.entity.Todo
import ru.terrakok.gitlabclient.model.data.state.ServerChanges

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 14.10.18.
 */
class ApiWithChangesRegistration(
    private val serverApi: GitlabApi,
    private val serverChanges: ServerChanges
) : GitlabApi by serverApi {

    override fun createMilestone(
        projectId: Long,
        title: String,
        description: String?,
        dueDate: LocalDate?,
        startDate: LocalDate?
    ): Single<Milestone> =
        serverApi.createMilestone(projectId, title, description, dueDate, startDate)
            .doOnSuccess { serverChanges.milestoneChanged(it.id) }

    override fun updateMilestone(
        projectId: Long,
        mileStoneId: Long,
        title: String?,
        description: String?,
        dueDate: LocalDate?,
        startDate: LocalDate?
    ): Single<Milestone> =
        serverApi.updateMilestone(projectId, mileStoneId, title, description, dueDate, startDate)
            .doOnSuccess { serverChanges.milestoneChanged(mileStoneId) }

    override fun deleteMilestone(projectId: Long, mileStoneId: Long): Completable =
        serverApi.deleteMilestone(projectId, mileStoneId)
            .doOnComplete { serverChanges.milestoneChanged(mileStoneId) }

    override fun createLabel(
        projectId: Long,
        name: String,
        color: String,
        description: String?,
        priority: Int?
    ): Single<Label> =
        serverApi.createLabel(projectId, name, color, description, priority)
            .doOnSuccess { serverChanges.labelChanged(it.id) }

    override fun deleteLabel(projectId: Long, name: String): Completable =
        serverApi.deleteLabel(projectId, name)
            .doOnComplete { serverChanges.labelChanged() }

    override fun subscribeToLabel(projectId: Long, labelId: Long): Single<Label> =
        serverApi.subscribeToLabel(projectId, labelId)
            .doOnSuccess { serverChanges.labelChanged(labelId) }

    override fun unsubscribeFromLabel(projectId: Long, labelId: Long): Single<Label> =
        serverApi.unsubscribeFromLabel(projectId, labelId)
            .doOnSuccess { serverChanges.labelChanged(labelId) }

    override fun addMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String?
    ): Completable =
        serverApi.addMember(projectId, userId, accessLevel, expiresDate)
            .doOnComplete { serverChanges.memberChanged(userId) }

    override fun editMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String?
    ): Completable =
        serverApi.editMember(projectId, userId, accessLevel, expiresDate)
            .doOnComplete { serverChanges.memberChanged(userId) }

    override fun deleteMember(projectId: Long, userId: Long): Completable =
        serverApi.deleteMember(projectId, userId)
            .doOnComplete { serverChanges.memberChanged(userId) }

    override fun markPendingTodoAsDone(id: Long): Single<Todo> =
        serverApi.markPendingTodoAsDone(id)
            .doOnSuccess { serverChanges.todoChanged(id) }

    override fun markAllPendingTodosAsDone(): Completable =
        serverApi.markAllPendingTodosAsDone()
            .doOnComplete { serverChanges.todoChanged() }

    override fun editIssue(
        projectId: Long,
        issueId: Long,
        stateEvent: IssueStateEvent
    ): Completable =
        serverApi.editIssue(projectId, issueId, stateEvent)
            .doOnComplete { serverChanges.issueChanged(issueId) }
}
