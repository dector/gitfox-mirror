package gitfox.model.interactor

import gitfox.entity.Label
import gitfox.model.data.server.GitlabApi
import gitfox.model.data.state.ServerChanges
import kotlinx.coroutines.flow.Flow

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.10.2018.
 */
class LabelInteractor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val defaultPageSize: Int
) {

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
