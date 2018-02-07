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

    companion object {
        private val README_FILE_NAME = "readme.md"
    }

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
            projectRepository.getRepositoryTree(projectId = id, branchName = branchName)
                    .map { treeNodes ->
                        treeNodes.find { it.name.contains(README_FILE_NAME, true) }
                                ?: throw NoSuchElementException("$README_FILE_NAME not found!")
                    }
                    .flatMap { treeNode ->
                        projectRepository.getFile(id, treeNode.name, branchName)
                                .observeOn(schedulers.computation())
                                .map { file -> base64Tools.decode(file.content) }
                                .observeOn(schedulers.ui())
                    }
}