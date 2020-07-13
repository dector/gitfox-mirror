package gitfox.model.data.server

import gitfox.entity.Label
import gitfox.entity.OrderBy
import gitfox.entity.Project
import gitfox.entity.Sort
import gitfox.entity.Visibility
import gitfox.model.data.cache.ProjectCache

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 14.10.18.
 */
internal class ApiWithProjectCache(
    private val serverApi: GitlabApi,
    private val projectCache: ProjectCache
) : GitlabApi by serverApi {

    override suspend fun getProjects(
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
    ): List<Project> =
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
            .also { projectCache.put(it) }

    override suspend fun getProject(
        id: Long,
        statistics: Boolean
    ): Project =
        projectCache.get(id) ?: serverApi.getProject(id, statistics)
            .also { projectCache.put(listOf(it)) }

}
