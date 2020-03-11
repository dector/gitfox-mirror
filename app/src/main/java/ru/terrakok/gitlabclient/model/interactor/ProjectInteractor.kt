package ru.terrakok.gitlabclient.model.interactor

import io.reactivex.Single
import kotlinx.coroutines.rx2.asObservable
import kotlinx.coroutines.rx2.rxSingle
import ru.terrakok.gitlabclient.di.DefaultPageSize
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.*
import ru.terrakok.gitlabclient.entity.app.ProjectFile
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.util.Base64Tools
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */
class ProjectInteractor @Inject constructor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val schedulers: SchedulersProvider,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) {
    private val defaultPageSize = defaultPageSizeWrapper.value
    private val base64Tools = Base64Tools()

    val projectChanges = serverChanges.projectChanges.asObservable().observeOn(schedulers.ui())

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
    ) = rxSingle {
        api.getProjects(
            archived,
            visibility,
            orderBy,
            sort,
            search,
            simple,
            owned,
            membership,
            starred,
            page,
            pageSize
        )
    }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getProject(id: Long) =
        rxSingle { api.getProject(id) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getProjectRawFile(projectId: Long, path: String, fileReference: String): Single<String> =
        getProjectFile(projectId, path, fileReference)
            .observeOn(schedulers.computation())
            .map { file -> base64Tools.decode(file.content) }
            .observeOn(schedulers.ui())

    fun getProjectReadme(project: Project) =
        Single
            .defer {
                if (project.defaultBranch != null && project.readmeUrl != null) {
                    val readmePath = project.readmeUrl.substringAfter(
                        "/blob/${project.defaultBranch}/"
                    )
                    getProjectFile(project.id, readmePath, project.defaultBranch)
                } else {
                    Single.error(ReadmeNotFound())
                }
            }
            .observeOn(schedulers.computation())
            .map { file -> base64Tools.decode(file.content) }
            .observeOn(schedulers.ui())

    fun getProjectFile(
        projectId: Long,
        path: String,
        fileReference: String
    ) = rxSingle { api.getFile(projectId, path, fileReference) }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getProjectFiles(
        projectId: Long,
        path: String,
        branchName: String,
        recursive: Boolean? = null,
        page: Int,
        pageSize: Int = defaultPageSize
    ): Single<List<ProjectFile>> =
        rxSingle { api.getRepositoryTree(projectId, path, branchName, recursive, page, pageSize) }
            .map { trees ->
                trees.map { tree ->
                    ProjectFile(
                        tree.id,
                        tree.name,
                        tree.type
                    )
                }
            }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getProjectBranches(
        projectId: Long
    ): Single<List<Branch>> =
        rxSingle { api.getRepositoryBranches(projectId) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    class ReadmeNotFound : Exception()
}
