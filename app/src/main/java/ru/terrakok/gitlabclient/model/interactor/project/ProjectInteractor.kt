package ru.terrakok.gitlabclient.model.interactor.project

import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository
import ru.terrakok.gitlabclient.model.repository.tools.Base64Tools
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.04.17.
 */
class ProjectInteractor @Inject constructor(
        private val projectRepository: ProjectRepository,
        private val schedulers: SchedulersProvider,
        private val base64Tools: Base64Tools
) {

    fun getMainProjects(page: Int) = projectRepository
            .getProjectsList(
                    page = page,
                    membership = true,
                    orderBy = OrderBy.LAST_ACTIVITY_AT,
                    archived = false
            )

    fun getMyProjects(page: Int) = projectRepository
            .getProjectsList(
                    page = page,
                    owned = true,
                    orderBy = OrderBy.LAST_ACTIVITY_AT,
                    archived = false
            )

    fun getStarredProjects(page: Int) = projectRepository
            .getProjectsList(
                    page = page,
                    starred = true,
                    orderBy = OrderBy.LAST_ACTIVITY_AT,
                    archived = false
            )

    fun getProject(id: Long) = projectRepository.getProject(id)

    fun getProjectReadme(id: Long, branchName: String) =
            projectRepository.getFile(id, "README.md", branchName)
                    .observeOn(schedulers.computation())
                    .map { file -> base64Tools.decode(file.content) }
                    .observeOn(schedulers.ui())
}