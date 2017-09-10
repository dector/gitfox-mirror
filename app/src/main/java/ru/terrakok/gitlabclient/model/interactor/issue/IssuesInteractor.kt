package ru.terrakok.gitlabclient.model.interactor.issue

import ru.terrakok.gitlabclient.entity.IssueState
import ru.terrakok.gitlabclient.model.repository.issue.IssueRepository
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
class IssuesInteractor @Inject constructor(
        private val issueRepository: IssueRepository
) {
    fun getMyIssues(
            isOpened: Boolean,
            page: Int
    ) = issueRepository
            .getMyIssues(
                    state = if (isOpened) IssueState.OPENED else IssueState.CLOSED,
                    page = page
            )
}