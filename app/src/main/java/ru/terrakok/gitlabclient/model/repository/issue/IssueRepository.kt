package ru.terrakok.gitlabclient.model.repository.issue

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import ru.terrakok.gitlabclient.entity.*
import ru.terrakok.gitlabclient.entity.app.target.AppTarget
import ru.terrakok.gitlabclient.entity.app.target.TargetHeader
import ru.terrakok.gitlabclient.entity.app.target.TargetHeaderIcon
import ru.terrakok.gitlabclient.entity.app.target.TargetHeaderTitle
import ru.terrakok.gitlabclient.entity.event.EventAction
import ru.terrakok.gitlabclient.entity.target.TargetState
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
            state: TargetState? = null,
            labels: String? = null,
            milestone: String? = null,
            iids: Array<Long>? = null,
            orderBy: OrderBy? = null,
            sort: Sort? = null,
            search: String? = null,
            page: Int,
            pageSize: Int = defaultPageSize
    ) = api
            .getMyIssues(scope, state, labels, milestone, iids, orderBy, sort, search, page, pageSize)
            .flatMap { issues ->
                Single.zip(
                        Single.just(issues),
                        getDistinctProjects(issues),
                        BiFunction<List<Issue>, Map<Long, Project>, List<TargetHeader>> { sourceIssues, projects ->
                            val items = mutableListOf<TargetHeader>()
                            sourceIssues.forEach {
                                items.add(getTargetHeader(it, projects[it.projectId]?.nameWithNamespace ?: "project"))
                            }
                            return@BiFunction items
                        }
                )
            }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    private fun getDistinctProjects(events: List<Issue>): Single<Map<Long, Project>> {
        return Observable.fromIterable(events)
                .distinct { it.projectId }
                .flatMapSingle { issue -> api.getProject(issue.projectId) }
                .toMap { it.id }
    }

    private fun getTargetHeader(issue: Issue, projectName: String) = TargetHeader(
            issue.author,
            TargetHeaderIcon.NONE,
            TargetHeaderTitle.Event(
                    issue.author.username ?: issue.author.name ?: "user",
                    EventAction.CREATED,
                    "${AppTarget.ISSUE} #${issue.iid}",
                    projectName
            ),
            issue.title,
            issue.createdAt,
            AppTarget.ISSUE,
            issue.id
    )
}