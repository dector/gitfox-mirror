package gitfox.model.interactor

import gitfox.entity.Label
import gitfox.model.data.cache.ProjectLabelCache
import gitfox.model.data.server.GitlabApi
import gitfox.model.data.state.ServerChanges
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.toList

/**
 * @author Maxim Myalkin (MaxMyalkin) on 30.10.2018.
 */
class LabelInteractor internal constructor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val defaultPageSize: Int,
    private val projectLabelCache: ProjectLabelCache
) {

    val labelChanges: Flow<Long> = serverChanges.labelChanges

    suspend fun getLabelList(
        projectId: Long,
        page: Int
    ): List<Label> = api.getProjectLabels(projectId, page, defaultPageSize)

    suspend fun getAllProjectLabels(projectId: Long): List<Label> {
        return projectLabelCache.getOrPut(projectId) {
            getAllProjectLabelsFromServer(projectId)
        }
    }

    private suspend fun getAllProjectLabelsFromServer(
        projectId: Long
    ): List<Label> =
        (0..Int.MAX_VALUE)
            .asFlow()
            .map { page -> api.getProjectLabels(projectId, page, defaultPageSize) }
            .takeWhile { it.isNotEmpty() }
            .flatMapConcat { it.asFlow() }
            .toList()

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
