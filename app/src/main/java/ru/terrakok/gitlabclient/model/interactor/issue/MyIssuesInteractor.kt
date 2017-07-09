package ru.terrakok.gitlabclient.model.interactor.issue

import ru.terrakok.gitlabclient.model.repository.issue.IssueRepository
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
class MyIssuesInteractor @Inject constructor(private val issueRepository: IssueRepository) {
    fun getMyIssues(page: Int) = issueRepository.getMyIssues(page)
}