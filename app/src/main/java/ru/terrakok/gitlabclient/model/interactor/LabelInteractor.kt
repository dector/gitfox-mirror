package ru.terrakok.gitlabclient.model.interactor

import ru.terrakok.gitlabclient.di.DefaultPageSize
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.10.2018.
 */
class LabelInteractor @Inject constructor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    @DefaultPageSize defaultPageSizeWrapper: PrimitiveWrapper<Int>,
    private val schedulers: SchedulersProvider
) {

    private val defaultPageSize: Int = defaultPageSizeWrapper.value

    val labelChanges = serverChanges.labelChanges

    fun getLabelList(
        projectId: Long,
        page: Int
    ) = api
        .getProjectLabels(projectId, page, defaultPageSize)
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