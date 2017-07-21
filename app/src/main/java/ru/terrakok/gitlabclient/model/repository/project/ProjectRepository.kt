package ru.terrakok.gitlabclient.model.repository.project

import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.interactor.projects.ProjectsListFilter
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */
class ProjectRepository @Inject constructor(private val api: GitlabApi,
                                            private val schedulers: SchedulersProvider) {

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
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())

    fun getProject(id: Long) =
            api.getProject(id)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())

    fun getFile(projectId: Long, path: String, branchName: String) =
            api.getFile(projectId, path, branchName)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
}