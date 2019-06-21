package ru.terrakok.gitlabclient.model.interactor.issue

import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.Sort
import ru.terrakok.gitlabclient.entity.issue.IssueScope
import ru.terrakok.gitlabclient.entity.issue.IssueState
import ru.terrakok.gitlabclient.model.repository.issue.IssueRepository
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 15.06.17.
 */
class IssueInteractor @Inject constructor(
    private val issueRepository: IssueRepository
) {

    val issueChanges = issueRepository.issueChanges

    fun getMyIssues(
        createdByMe: Boolean,
        onlyOpened: Boolean,
        page: Int
    ) = issueRepository
        .getMyIssues(
            scope = if (createdByMe) IssueScope.CREATED_BY_ME else IssueScope.ASSIGNED_BY_ME,
            state = if (onlyOpened) IssueState.OPENED else null,
            orderBy = OrderBy.UPDATED_AT,
            page = page
        )

    fun getIssues(
        projectId: Long,
        issueState: IssueState?,
        page: Int
    ) = issueRepository
        .getIssues(
            projectId = projectId,
            state = issueState,
            scope = IssueScope.ALL,
            orderBy = OrderBy.UPDATED_AT,
            page = page
        )

    fun getIssue(
        projectId: Long,
        issueId: Long
    ) = issueRepository.getIssue(projectId, issueId)

    fun getIssueNotes(
        projectId: Long,
        issueId: Long,
        page: Int
    ) = issueRepository.getIssueNotes(projectId, issueId, Sort.ASC, OrderBy.UPDATED_AT, page)

    fun getAllIssueNotes(
        projectId: Long,
        issueId: Long
    ) = issueRepository.getAllIssueNotes(projectId, issueId, Sort.ASC, OrderBy.UPDATED_AT)

    fun createIssueNote(
        projectId: Long,
        issueId: Long,
        body: String
    ) = issueRepository.createIssueNote(projectId, issueId, body)
}