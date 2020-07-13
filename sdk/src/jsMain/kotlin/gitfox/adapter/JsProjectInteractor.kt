package gitfox.adapter

import gitfox.entity.OrderBy
import gitfox.entity.Project
import gitfox.entity.Sort
import gitfox.entity.Visibility
import gitfox.model.interactor.ProjectInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

class JsProjectInteractor internal constructor(
    private val interactor: ProjectInteractor,
    private val defaultPageSize: Int
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    @JsName("projectChanges")
    val projectChanges = interactor.projectChanges.wrap()

    @JsName("getProjectsList")
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
        pageSize: Int = defaultPageSize
    ) = promise {
        interactor.getProjectsList(archived, visibility, orderBy, sort, search, simple, owned, membership, starred, page, pageSize)
    }

    @JsName("getProject")
    fun getProject(
        id: Long
    ) = promise {
        interactor.getProject(id)
    }

    @JsName("getProjectRawFile")
    fun getProjectRawFile(
        projectId: Long,
        path: String,
        fileReference: String
    ) = promise {
        interactor.getProjectRawFile(projectId, path, fileReference)
    }

    @JsName("getProjectReadme")
    fun getProjectReadme(
        project: Project
    ) = promise {
        interactor.getProjectReadme(project)
    }

    @JsName("getProjectFile")
    fun getProjectFile(
        projectId: Long,
        path: String,
        fileReference: String
    ) = promise {
        interactor.getProjectFile(projectId, path, fileReference)
    }

    @JsName("getProjectFiles")
    fun getProjectFiles(
        projectId: Long,
        path: String,
        branchName: String,
        recursive: Boolean? = null,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = promise {
        interactor.getProjectFiles(projectId, path, branchName, recursive, page, pageSize)
    }

    @JsName("getProjectBranches")
    fun getProjectBranches(
        projectId: Long
    ) = promise {
        interactor.getProjectBranches(projectId)
    }
}