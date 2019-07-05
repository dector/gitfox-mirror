package ru.terrakok.gitlabclient.model.data.server

import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.Sort
import ru.terrakok.gitlabclient.entity.Visibility
import ru.terrakok.gitlabclient.model.data.cache.ProjectCache

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 14.10.18.
 */
class ApiWithProjectCache(
    private val serverApi: GitlabApi,
    private val projectCache: ProjectCache
) : GitlabApi by serverApi {

    override fun getProjects(
        archived: Boolean?,
        visibility: Visibility?,
        orderBy: OrderBy?,
        sort: Sort?,
        search: String?,
        simple: Boolean?,
        owned: Boolean?,
        membership: Boolean?,
        starred: Boolean?,
        page: Int,
        pageSize: Int
    ): Single<List<Project>> =
        serverApi
            .getProjects(
                archived,
                visibility,
                orderBy,
                sort,
                search,
                simple,
                owned,
                membership,
                starred,
                page,
                pageSize
            )
            .doOnSuccess { projectCache.put(it) }

    override fun getProject(
        id: Long,
        statistics: Boolean
    ): Single<Project> =
        Single
            .defer {
                val cachedProject = projectCache.get(id)
                if (cachedProject == null) {
                    serverApi.getProject(id, statistics)
                        .doOnSuccess { projectCache.put(listOf(it)) }
                } else {
                    Single.just(cachedProject)
                }
            }
}