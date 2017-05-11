package ru.terrakok.gitlabclient.model.interactor.project

import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.04.17.
 */
class ProjectInteractor(private val projectRepository: ProjectRepository) {

    fun getProject(id: Long) = projectRepository.getProject(id)

    fun getProjectReadmeFile(id: Long, branchName: String)
            = projectRepository.getFile(id, "README.md", branchName)
}