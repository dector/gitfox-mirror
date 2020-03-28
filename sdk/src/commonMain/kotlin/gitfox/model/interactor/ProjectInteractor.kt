package gitfox.model.interactor

import gitfox.entity.*
import gitfox.entity.app.ProjectFile
import gitfox.model.data.server.GitlabApi
import gitfox.model.data.state.ServerChanges
import gitfox.util.Base64Tools
import kotlinx.coroutines.flow.Flow

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */
class ProjectInteractor internal constructor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val defaultPageSize: Int
) {
    private val base64Tools = Base64Tools()

    val projectChanges: Flow<Long> = serverChanges.projectChanges

    suspend fun getProjectsList(
        archived: Boolean? = null,
        visibility: Visibility? = null,
        orderBy: OrderBy? = null,
        sort: Sort? = null,
        search: String? = null,
        simple: Boolean? = null,
        owned: Boolean? = null,
        membership: Boolean? = null,
        starred: Boolean? = null,
        page: Int,
        pageSize: Int = defaultPageSize
    ): List<Project> =
        api.getProjects(
            archived, visibility, orderBy, sort, search, simple,
            owned, membership, starred, page, pageSize
        )

    suspend fun getProject(id: Long): Project = api.getProject(id)

    suspend fun getProjectRawFile(projectId: Long, path: String, fileReference: String): String {
        val file = getProjectFile(projectId, path, fileReference)
        return base64Tools.decode(file.content)
    }

    suspend fun getProjectReadme(project: Project): String {
        if (project.defaultBranch != null && project.readmeUrl != null) {
            val readmePath = project.readmeUrl.substringAfter(
                "/blob/${project.defaultBranch}/"
            )
            val file = getProjectFile(project.id, readmePath, project.defaultBranch)
            return base64Tools.decode(file.content)
        } else {
            throw ReadmeNotFound()
        }
    }

    suspend fun getProjectFile(
        projectId: Long,
        path: String,
        fileReference: String
    ): File = api.getFile(projectId, path, fileReference)

    suspend fun getProjectFiles(
        projectId: Long,
        path: String,
        branchName: String,
        recursive: Boolean? = null,
        page: Int,
        pageSize: Int = defaultPageSize
    ): List<ProjectFile> =
        api.getRepositoryTree(projectId, path, branchName, recursive, page, pageSize)
            .map { tree -> ProjectFile(tree.id, tree.name, tree.type) }


    suspend fun getProjectBranches(
        projectId: Long
    ): List<Branch> = api.getRepositoryBranches(projectId)
}
