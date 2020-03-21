package ru.terrakok.gitlabclient.model.interactor

import ru.terrakok.gitlabclient.entity.Commit
import ru.terrakok.gitlabclient.entity.DiffData
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import javax.inject.Inject

/**
 * @author Valentin Logvinovitch (glvvl) on 18.06.19.
 */
class CommitInteractor @Inject constructor(
    private val api: GitlabApi
) {

    suspend fun getCommit(projectId: Long, commitId: String): Commit =
        api.getRepositoryCommit(projectId, commitId)

    suspend fun getCommitDiffData(projectId: Long, commitId: String): List<DiffData> =
        api.getCommitDiffData(projectId, commitId)
}
