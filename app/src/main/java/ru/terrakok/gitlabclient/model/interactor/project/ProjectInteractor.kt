package ru.terrakok.gitlabclient.model.interactor.project

import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.Project
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

    fun getProjectReadme(project: Project) =
        Single
            .defer {
                if (project.readmeUrl != null) {
                    val readmePath = project.readmeUrl.substringAfter(
                        "${project.webUrl}/blob/${project.defaultBranch}/"
                    )
                    projectRepository.getFile(project.id, readmePath, project.defaultBranch)
                } else {
                    Single.error(ReadmeNotFound())
                }
            }
            .observeOn(schedulers.computation())
            .map { file -> base64Tools.decode(file.content) }
            .observeOn(schedulers.ui())


    fun getCommitDiff(
        projectId: Long,
        commitId: String
    ) = projectRepository.getCommitDiff(projectId, commitId)

    class ReadmeNotFound : Exception()
}