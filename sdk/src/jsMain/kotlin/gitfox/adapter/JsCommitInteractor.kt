package gitfox.adapter

import gitfox.model.interactor.CommitInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

class JsCommitInteractor internal constructor(
    private val interactor: CommitInteractor
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    @JsName("getCommit")
    fun getCommit(
        projectId: Long,
        commitId: String
    ) = promise {
        interactor.getCommit(projectId, commitId)
    }

    @JsName("getCommitDiffData")
    fun getCommitDiffData(
        projectId: Long,
        commitId: String
    ) = promise {
        interactor.getCommitDiffData(projectId, commitId)
    }
}