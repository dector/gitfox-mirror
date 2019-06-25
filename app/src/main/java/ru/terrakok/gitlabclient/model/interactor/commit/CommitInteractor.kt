package ru.terrakok.gitlabclient.model.interactor.commit

import ru.terrakok.gitlabclient.model.repository.commit.CommitRepository
import javax.inject.Inject

/**
 * @author Valentin Logvinovitch (glvvl) on 18.06.19.
 */
class CommitInteractor @Inject constructor(
    private val commitRepository: CommitRepository
) {

    fun getCommit(projectId: Long, commitId: String) =
            commitRepository.getCommit(projectId, commitId)

    fun getCommitDiffData(projectId: Long, commitId: String) =
            commitRepository.getCommitDiffData(projectId, commitId)
}