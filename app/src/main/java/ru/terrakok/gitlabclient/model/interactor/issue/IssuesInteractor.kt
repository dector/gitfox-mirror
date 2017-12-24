package ru.terrakok.gitlabclient.model.interactor.issue

import ru.terrakok.gitlabclient.entity.IssueScope
import ru.terrakok.gitlabclient.model.repository.issue.IssueRepository
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
class IssuesInteractor @Inject constructor(
        private val issueRepository: IssueRepository
) {
    fun getMyIssues(
            createdByMe: Boolean,
            page: Int
    ) = issueRepository
            .getMyIssues(
                    scope = if (createdByMe) IssueScope.CREATED_BY_ME else IssueScope.ASSIGNED_BY_ME,
                    page = page
            )
}