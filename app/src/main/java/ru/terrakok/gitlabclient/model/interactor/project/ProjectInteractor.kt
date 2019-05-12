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

    fun getProjectFileRawCode(projectId: Long, path: String, branchName: String): Single<String> =
        projectRepository.getBlobFile(projectId, path, branchName)
            .observeOn(schedulers.computation())
            .map { file -> base64Tools.decode(file.content) }
            .observeOn(schedulers.ui())

    fun getProjectReadme(project: Project) =
        Single
            .defer {
                if (project.defaultBranch != null && project.readmeUrl != null) {
                    val readmePath = project.readmeUrl.substringAfter(
                        "${project.webUrl}/blob/${project.defaultBranch}/"
                    )
                    projectRepository.getBlobFile(project.id, readmePath, project.defaultBranch)
                } else {
                    Single.error(ReadmeNotFound())
                }
            }
            .observeOn(schedulers.computation())
            .map { file -> base64Tools.decode(file.content) }
            .observeOn(schedulers.ui())

    fun getProjectFiles(
        projectId: Long,
        path: String,
        branchName: String,
        page: Int
    ) = projectRepository.getProjectFiles(projectId = projectId, path = path, branchName = branchName, page = page)

    fun getProjectBranches(
        projectId: Long
    ) = projectRepository.getProjectBranches(projectId)

    class ReadmeNotFound : Exception()
}