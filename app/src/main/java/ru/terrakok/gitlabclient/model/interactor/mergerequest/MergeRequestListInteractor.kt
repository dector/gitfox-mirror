package ru.terrakok.gitlabclient.model.interactor.mergerequest

import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.mergerequest.MergeRequestState
import ru.terrakok.gitlabclient.model.repository.mergerequest.MergeRequestRepository
import javax.inject.Inject

class MergeRequestListInteractor @Inject constructor(
        private val mergeRequestRepository: MergeRequestRepository
) {
    fun getMyMergeRequests(
            state: MergeRequestState,
            page: Int
    ) = mergeRequestRepository
            .getMergeRequests(
                    state = state,
                    page = page,
                    orderBy = OrderBy.UPDATED_AT
            )
}