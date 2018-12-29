package ru.terrakok.gitlabclient.model.repository.project

import io.reactivex.Observable
import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.Branch
import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.Sort
import ru.terrakok.gitlabclient.entity.Visibility
import ru.terrakok.gitlabclient.entity.app.ProjectFile
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultPageSize
import javax.inject.Inject

/**
 * @author Konstantin Tskhovrebov (aka terrakok) on 24.04.17.
 */
class ProjectRepository @Inject constructor(
    private val api: GitlabApi,
    private val schedulers: SchedulersProvider,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) {
    private val defaultPageSize = defaultPageSizeWrapper.value
    private val projectLabels = mutableMapOf<Long, Observable<List<Label>>>()

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
    ) = api
        .getProjects(
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
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getProject(id: Long) = api
        .getProject(id)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getBlobFile(
        projectId: Long,
        path: String,
        branchName: String
    ) = api
        .getFile(projectId, path, branchName)
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
        api
            .getRepositoryTree(projectId, path, branchName, recursive, page, pageSize)
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
        api
            .getRepositoryBranches(projectId)
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getProjectLabels(projectId: Long): Single<List<Label>> {
        return projectLabels.getOrPut(projectId) {
            api
                .getProjectLabels(projectId)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.ui())
                .toObservable()
                .cache()
                .share()
        }.singleOrError()
    }
}