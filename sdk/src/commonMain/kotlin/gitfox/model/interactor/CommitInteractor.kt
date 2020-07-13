package gitfox.model.interactor

import gitfox.entity.Commit
import gitfox.entity.DiffData
import gitfox.model.data.server.GitlabApi

/**
 * @author Valentin Logvinovitch (glvvl) on 18.06.19.
 */
class CommitInteractor internal constructor(
    private val api: GitlabApi
) {

    suspend fun getCommit(projectId: Long, commitId: String): Commit =
        api.getRepositoryCommit(projectId, commitId)

    suspend fun getCommitDiffData(projectId: Long, commitId: String): List<DiffData> =
        api.getCommitDiffData(projectId, commitId)
}
