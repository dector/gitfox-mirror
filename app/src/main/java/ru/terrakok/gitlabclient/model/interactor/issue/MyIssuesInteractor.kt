package ru.terrakok.gitlabclient.model.interactor.issue

import ru.terrakok.gitlabclient.model.repository.issue.IssueRepository

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
class MyIssuesInteractor(private val issueRepository: IssueRepository) {
    fun getMyIssues(page: Int) = issueRepository.getMyIssues(page)
}