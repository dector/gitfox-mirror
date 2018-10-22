package ru.terrakok.gitlabclient.model.interactor.mergerequest

import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.Sort
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestScope
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.model.repository.mergerequest.MergeRequestRepository
import javax.inject.Inject

class MergeRequestInteractor @Inject constructor(
    private val mergeRequestRepository: MergeRequestRepository
) {
    fun getMyMergeRequests(
        createdByMe: Boolean,
        onlyOpened: Boolean,
        page: Int
    ) = mergeRequestRepository
        .getMyMergeRequests(
            scope = if (createdByMe) MergeRequestScope.CREATED_BY_ME else MergeRequestScope.ASSIGNED_TO_ME,
            state = if (onlyOpened) MergeRequestState.OPENED else null,
            orderBy = OrderBy.UPDATED_AT,
            page = page
        )

    fun getMergeRequests(
        projectId: Long,
        mergeRequestState: MergeRequestState,
        page: Int
    ) = mergeRequestRepository
        .getMergeRequests(
            projectId = projectId,
            state = mergeRequestState,
            orderBy = OrderBy.UPDATED_AT,
            page = page
        )

    fun getMergeRequest(
        projectId: Long,
        mergeRequestId: Long
    ) = mergeRequestRepository.getMergeRequest(projectId, mergeRequestId)

    fun getMergeRequestNotes(
        projectId: Long,
        mergeRequestId: Long,
        page: Int
    ) = mergeRequestRepository.getMergeRequestNotes(projectId, mergeRequestId, Sort.ASC, OrderBy.UPDATED_AT, page)

    fun getAllMergeRequestNotes(
        projectId: Long,
        mergeRequestId: Long
    ) = mergeRequestRepository.getAllMergeRequestNotes(projectId, mergeRequestId, Sort.ASC, OrderBy.UPDATED_AT)

    fun createMergeRequestNote(
        projectId: Long,
        issueId: Long,
        body: String
    ) = mergeRequestRepository.createMergeRequestNote(projectId, issueId, body)

    fun getMergeRequestCommits(
        projectId: Long,
        mergeRequestId: Long,
        page: Int
    ) = mergeRequestRepository.getMergeRequestCommits(projectId, mergeRequestId, page)
}