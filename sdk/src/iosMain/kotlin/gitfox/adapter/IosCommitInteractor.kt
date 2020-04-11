package gitfox.adapter

import gitfox.entity.Commit
import gitfox.entity.DiffData
import gitfox.model.interactor.CommitInteractor
import kotlinx.coroutines.CoroutineScope

class IosCommitInteractor internal constructor(
    private val interactor: CommitInteractor
) : CoroutineScope by CoroutineScope(MainLoopDispatcher) {

    fun getCommit(
        projectId: Long,
        commitId: String,
        callback: (result: Commit?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getCommit(projectId, commitId) }
    }

    fun getCommitDiffData(
        projectId: Long,
        commitId: String,
        callback: (result: List<DiffData>?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getCommitDiffData(projectId, commitId) }
    }
}