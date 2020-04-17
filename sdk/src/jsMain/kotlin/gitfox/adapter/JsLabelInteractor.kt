package gitfox.adapter

import gitfox.model.interactor.LabelInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

class JsLabelInteractor internal constructor(
    private val interactor: LabelInteractor
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    @JsName("getLabelList")
    fun getLabelList(
        projectId: Long,
        page: Int
    ) = promise {
        interactor.getLabelList(projectId, page)
    }

    @JsName("createLabel")
    fun createLabel(
        projectId: Long,
        name: String,
        color: String,
        description: String?,
        priority: Int?
    ) = promise {
        interactor.createLabel(projectId, name, color, description, priority)
    }

    @JsName("deleteLabel")
    fun deleteLabel(
        projectId: Long,
        name: String
    ) = promise {
        interactor.deleteLabel(projectId, name)
    }

    @JsName("subscribeToLabel")
    fun subscribeToLabel(
        projectId: Long,
        labelId: Long
    ) = promise {
        interactor.subscribeToLabel(projectId, labelId)
    }

    @JsName("unsubscribeFromLabel")
    fun unsubscribeFromLabel(
        projectId: Long,
        labelId: Long
    ) = promise {
        interactor.unsubscribeFromLabel(projectId, labelId)
    }
}