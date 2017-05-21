package ru.terrakok.gitlabclient.model.interactor.projects

import ru.terrakok.gitlabclient.entity.app.ProjectsListFilter
import ru.terrakok.gitlabclient.entity.common.OrderBy
import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */
class MainProjectsListInteractor(private val projectRepository: ProjectRepository) {

    fun getMainProjects(page: Int) =
            projectRepository
                    .getProjectsList(page = page,
                            filter = ProjectsListFilter(
                                    membership = true,
                                    order_by = OrderBy.LAST_ACTIVITY_AT,
                                    archived = false))

    fun getMyProjects(page: Int) =
            projectRepository
                    .getProjectsList(page = page,
                            filter = ProjectsListFilter(
                                    owned = true,
                                    order_by = OrderBy.LAST_ACTIVITY_AT,
                                    archived = false))

    fun getStarredProjects(page: Int) =
            projectRepository
                    .getProjectsList(page = page,
                            filter = ProjectsListFilter(
                                    starred = true,
                                    order_by = OrderBy.LAST_ACTIVITY_AT,
                                    archived = false))
}