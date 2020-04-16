package gitfox.adapter

import gitfox.model.interactor.LabelInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

class JsLabelInteractor internal constructor(
    private val interactor: LabelInteractor
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    fun getLabelList(
        projectId: Long,
        page: Int
    ) = promise {
        interactor.getLabelList(projectId, page)
    }

    fun createLabel(
        projectId: Long,
        name: String,
        color: String,
        description: String?,
        priority: Int?
    ) = promise {
        interactor.createLabel(projectId, name, color, description, priority)
    }

    fun deleteLabel(
        projectId: Long,
        name: String
    ) = promise {
        interactor.deleteLabel(projectId, name)
    }

    fun subscribeToLabel(
        projectId: Long,
        labelId: Long
    ) = promise {
        interactor.subscribeToLabel(projectId, labelId)
    }

    fun unsubscribeFromLabel(
        projectId: Long,
        labelId: Long
    ) = promise {
        interactor.unsubscribeFromLabel(projectId, labelId)
    }
}