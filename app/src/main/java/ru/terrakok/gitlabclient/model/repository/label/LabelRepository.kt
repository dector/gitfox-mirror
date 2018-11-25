package ru.terrakok.gitlabclient.model.repository.label

import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.10.2018.
 */
class LabelRepository @Inject constructor(
    private val api: GitlabApi,
    private val schedulers: SchedulersProvider
) {
    fun getLabelList(
        projectId: Long,
        page: Int
    ) = api
        .getProjectLabels(projectId, page, GitlabApi.MAX_PAGE_SIZE)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun createLabel(
        projectId: Long,
        name: String,
        color: String,
        description: String?,
        priority: Int?
    ) = api
        .createLabel(projectId, name, color, description, priority)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun deleteLabel(
        projectId: Long,
        name: String
    ) = api
        .deleteLabel(projectId, name)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun subscribeToLabel(
        projectId: Long,
        labelId: Long
    ) = api
        .subscribeToLabel(projectId, labelId)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun unsubscribeFromLabel(
        projectId: Long,
        labelId: Long
    ) = api
        .unsubscribeFromLabel(projectId, labelId)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
}