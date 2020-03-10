package ru.terrakok.gitlabclient.model.interactor

import kotlinx.coroutines.rx2.rxSingle
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

/**
 * @author Valentin Logvinovitch (glvvl) on 18.06.19.
 */
class CommitInteractor @Inject constructor(
    private val api: GitlabApi,
    private val schedulers: SchedulersProvider
) {

    fun getCommit(projectId: Long, commitId: String) =
        rxSingle { api.getRepositoryCommit(projectId, commitId) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getCommitDiffData(projectId: Long, commitId: String) =
        rxSingle { api.getCommitDiffData(projectId, commitId) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}
