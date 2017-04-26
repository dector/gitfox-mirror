package ru.terrakok.gitlabclient.model.project

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.terrakok.gitlabclient.model.server.GitlabApi
import ru.terrakok.gitlabclient.model.server.ServerConfig

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */
class ProjectRepository(
        private val api: GitlabApi,
        private val serverConfig: ServerConfig) {

    fun getProjectsList(filter: ProjectsListFilter, page: Int, pageSize: Int = 20) =
            api.getProjects(
                    filter.archived,
                    filter.visibility,
                    filter.order_by,
                    filter.sort,
                    filter.search,
                    filter.simple,
                    filter.owned,
                    filter.membership,
                    filter.starred,
                    page,
                    pageSize)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    fun getProject(id: Long) =
            api.getProject(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    fun getRawFilePath(projectId: Long, path: String) = serverConfig.getRawFilePath(projectId, path)
}