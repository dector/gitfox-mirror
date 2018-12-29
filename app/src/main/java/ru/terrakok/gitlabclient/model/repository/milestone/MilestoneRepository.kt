package ru.terrakok.gitlabclient.model.repository.milestone

import io.reactivex.Completable
import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.milestone.Milestone
import ru.terrakok.gitlabclient.entity.milestone.MilestoneState
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultPageSize
import javax.inject.Inject

class MilestoneRepository @Inject constructor(
    private val api: GitlabApi,
    private val schedulers: SchedulersProvider,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) {
    private val defaultPageSize = defaultPageSizeWrapper.value

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
        dueDate: String? = null,
        startDate: String? = null
    ): Single<Milestone> = api
        .createMileStone(projectId, title, description, dueDate, startDate)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun updateMilestone(
        projectId: Long,
        milestoneId: Long,
        title: String? = null,
        description: String? = null,
        dueDate: String? = null,
        startDate: String? = null
    ): Single<Milestone> = api
        .updateMileStone(projectId, milestoneId, title, description, dueDate, startDate)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun deleteMilestone(
        projectId: Long,
        milestoneId: Long
    ): Completable = api
        .deleteMilestone(projectId, milestoneId)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
}