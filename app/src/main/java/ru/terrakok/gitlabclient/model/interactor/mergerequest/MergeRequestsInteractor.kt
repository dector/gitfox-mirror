package ru.terrakok.gitlabclient.model.interactor.mergerequest

import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestScope
import ru.terrakok.gitlabclient.model.repository.mergerequest.MergeRequestRepository
import javax.inject.Inject

class MergeRequestsInteractor @Inject constructor(
        private val mergeRequestRepository: MergeRequestRepository
) {
    fun getMyMergeRequests(
            createdByMe: Boolean,
            page: Int
    ) = mergeRequestRepository
            .getMergeRequests(
                    scope = if (createdByMe) MergeRequestScope.CREATED_BY_ME else MergeRequestScope.ASSIGNED_TO_ME,
                    page = page,
                    orderBy = OrderBy.UPDATED_AT
            )
}