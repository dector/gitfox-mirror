package ru.terrakok.gitlabclient.model.interactor

import kotlinx.coroutines.flow.Flow
import ru.terrakok.gitlabclient.di.DefaultPageSize
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.Label
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
import javax.inject.Inject

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.10.2018.
 */
class LabelInteractor @Inject constructor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    @DefaultPageSize defaultPageSizeWrapper: PrimitiveWrapper<Int>
) {

    private val defaultPageSize: Int = defaultPageSizeWrapper.value

    val labelChanges: Flow<Long> = serverChanges.labelChanges

    suspend fun getLabelList(
        projectId: Long,
        page: Int
    ): List<Label> = api.getProjectLabels(projectId, page, defaultPageSize)

    suspend fun createLabel(
        projectId: Long,
        name: String,
        color: String,
        description: String?,
        priority: Int?
    ): Label = api.createLabel(projectId, name, color, description, priority)

    suspend fun deleteLabel(
        projectId: Long,
        name: String
    ) {
        api.deleteLabel(projectId, name)
    }

    suspend fun subscribeToLabel(
        projectId: Long,
        labelId: Long
    ): Label = api.subscribeToLabel(projectId, labelId)

    suspend fun unsubscribeFromLabel(
        projectId: Long,
        labelId: Long
    ): Label = api.unsubscribeFromLabel(projectId, labelId)
}
