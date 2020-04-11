package gitfox.adapter

import gitfox.entity.*
import gitfox.entity.app.ProjectFile
import gitfox.model.interactor.ProjectInteractor
import kotlinx.coroutines.CoroutineScope

class IosProjectInteractor internal constructor(
    private val interactor: ProjectInteractor,
    private val defaultPageSize: Int
) : CoroutineScope by CoroutineScope(MainLoopDispatcher) {
    //    val projectChanges: Flow<Long>

    fun getProjectsList(
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
        pageSize: Int = defaultPageSize,
        callback: (result: List<Project>?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getProjectsList(archived, visibility, orderBy, sort, search, simple, owned, membership, starred, page, pageSize) }
    }

    fun getProject(
        id: Long,
        callback: (result: Project?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getProject(id) }
    }

    fun getProjectRawFile(
        projectId: Long,
        path: String,
        fileReference: String,
        callback: (result: String?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getProjectRawFile(projectId, path, fileReference) }
    }

    fun getProjectReadme(
        project: Project,
        callback: (result: String?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getProjectReadme(project) }
    }

    fun getProjectFile(
        projectId: Long,
        path: String,
        fileReference: String,
        callback: (result: File?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getProjectFile(projectId, path, fileReference) }
    }

    fun getProjectFiles(
        projectId: Long,
        path: String,
        branchName: String,
        recursive: Boolean? = null,
        page: Int,
        pageSize: Int = defaultPageSize,
        callback: (result: List<ProjectFile>?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getProjectFiles(projectId, path, branchName, recursive, page, pageSize) }
    }

    fun getProjectBranches(
        projectId: Long,
        callback: (result: List<Branch>?, error: Exception?) -> Unit
    ) {
        fire(callback) { interactor.getProjectBranches(projectId) }
    }
}