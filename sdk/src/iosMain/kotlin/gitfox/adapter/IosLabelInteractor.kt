package gitfox.adapter

import gitfox.entity.Label
import gitfox.model.interactor.LabelInteractor
import kotlinx.coroutines.CoroutineScope

class IosLabelInteractor internal constructor(
    private val interactor: LabelInteractor
) : CoroutineScope by CoroutineScope(MainLoopDispatcher) {

    fun getLabelList(
        projectId: Long,
        page: Int,
        callback: (result: List<Label>?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.getLabelList(projectId, page) }
    }

    fun createLabel(
        projectId: Long,
        name: String,
        color: String,
        description: String?,
        priority: Int?,
        callback: (result: Label?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.createLabel(projectId, name, color, description, priority) }
    }

    fun deleteLabel(
        projectId: Long,
        name: String,
        callback: (result: Unit?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.deleteLabel(projectId, name) }
    }

    fun subscribeToLabel(
        projectId: Long,
        labelId: Long,
        callback: (result: Label?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.subscribeToLabel(projectId, labelId) }
    }

    fun unsubscribeFromLabel(
        projectId: Long,
        labelId: Long,
        callback: (result: Label?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.unsubscribeFromLabel(projectId, labelId) }
    }
}