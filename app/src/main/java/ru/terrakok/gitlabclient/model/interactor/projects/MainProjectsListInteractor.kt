package ru.terrakok.gitlabclient.model.interactor.projects

import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */
class MainProjectsListInteractor @Inject constructor(private val projectRepository: ProjectRepository) {

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