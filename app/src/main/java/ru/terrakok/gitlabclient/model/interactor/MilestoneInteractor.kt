package ru.terrakok.gitlabclient.model.interactor

import io.reactivex.Completable
import io.reactivex.Single
import org.threeten.bp.LocalDate
import ru.terrakok.gitlabclient.di.DefaultPageSize
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.issue.Issue
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequest
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.entity.milestone.MilestoneState
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

class MilestoneInteractor @Inject constructor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val schedulers: SchedulersProvider,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) {
    private val defaultPageSize = defaultPageSizeWrapper.value

    val milestoneChanges = serverChanges.milestoneChanges

    fun getMilestones(
        projectId: Long,
        state: MilestoneState? = null,
        page: Int,
        pageSize: Int = defaultPageSize
    ): Single<List<Milestone>> = api
        .getMilestones(projectId, state, page, pageSize)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getMilestone(
        projectId: Long,
        milestoneId: Long
    ): Single<Milestone> = api
        .getMilestone(projectId, milestoneId)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun createMilestone(
        projectId: Long,
        title: String,
        description: String? = null,
        dueDate: LocalDate? = null,
        startDate: LocalDate? = null
    ): Single<Milestone> = api
        .createMilestone(projectId, title, description, dueDate, startDate)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun updateMilestone(
        projectId: Long,
        milestoneId: Long,
        title: String? = null,
        description: String? = null,
        dueDate: LocalDate? = null,
        startDate: LocalDate? = null
    ): Single<Milestone> = api
        .updateMilestone(projectId, milestoneId, title, description, dueDate, startDate)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun deleteMilestone(
        projectId: Long,
        milestoneId: Long
    ): Completable = api
        .deleteMilestone(projectId, milestoneId)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getMilestoneIssues(
        projectId: Long,
        milestoneId: Long,
        page: Int,
        pageSize: Int = defaultPageSize
    ): Single<List<Issue>> = api
        .getMilestoneIssues(projectId, milestoneId, page, pageSize)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getMilestoneMergeRequests(
        projectId: Long,
        milestoneId: Long,
        page: Int,
        pageSize: Int = defaultPageSize
    ): Single<List<MergeRequest>> = api
        .getMilestoneMergeRequests(projectId, milestoneId, page, pageSize)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
}