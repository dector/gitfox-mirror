package ru.terrakok.gitlabclient.model.repository.issue

import ru.terrakok.gitlabclient.entity.IssueScope
import ru.terrakok.gitlabclient.entity.IssueState
import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.Sort
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultPageSize
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 14.06.17.
 */
class IssueRepository @Inject constructor(
        private val api: GitlabApi,
        private val schedulers: SchedulersProvider,
        @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) {
    private val defaultPageSize = defaultPageSizeWrapper.value

    fun getMyIssues(
            scope: IssueScope? = null,
            state: IssueState? = null,
            labels: String? = null,
            milestone: String? = null,
            iids: Array<Long>? = null,
            orderBy: OrderBy? = null,
            sort: Sort? = null,
            search: String? = null,
            page: Int,
            pageSize: Int = defaultPageSize
    ) = api
            .getMyIssues(
                    scope,
                    state,
                    labels,
                    milestone,
                    iids,
                    orderBy,
                    sort,
                    search,
                    page,
                    pageSize
            )
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}