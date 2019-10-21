package ru.terrakok.gitlabclient.model.repository.commit

import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

/**
 * @author Valentin Logvinovitch (glvvl) on 18.06.19.
 */
class CommitRepository @Inject constructor(
    private val api: GitlabApi,
    private val schedulers: SchedulersProvider
) {

    fun getCommit(
        projectId: Long,
        commitId: String
    ) =
            api
                    .getRepositoryCommit(projectId, commitId)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())

    fun getCommitDiffData(
        projectId: Long,
        commitId: String
    ) =
            api
                    .getCommitDiffData(projectId, commitId)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
}