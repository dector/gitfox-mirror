package ru.terrakok.gitlabclient.model.interactor.project

import ru.terrakok.gitlabclient.model.repository.project.ProjectRepository
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 26.04.17.
 */
class ProjectInteractor @Inject constructor(private val projectRepository: ProjectRepository,
                                            private val mdConverter: MarkDownConverter,
                                            private val schedulers: SchedulersProvider,
                                            private val base64Tools: Base64Tools) {

    fun getProject(id: Long) = projectRepository.getProject(id)

    fun getProjectReadmeHtml(id: Long, branchName: String) =
            projectRepository.getFile(id, "README.md", branchName)
                    .observeOn(schedulers.computation())
                    .map { file ->
                        val md = base64Tools.decode(file.content)
                        val html = mdConverter.markdownToHtml(md)
                        html
                    }
                    .observeOn(schedulers.ui())
}