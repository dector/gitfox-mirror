package ru.terrakok.gitlabclient.model.repository.project

import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.OrderBy
import ru.terrakok.gitlabclient.entity.RepositoryTreeNodeType
import ru.terrakok.gitlabclient.entity.Sort
import ru.terrakok.gitlabclient.entity.Visibility
import ru.terrakok.gitlabclient.entity.app.RepositoryFile
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

    fun getFile(
        projectId: Long,
        path: String,
        branchName: String
    ) = api
        .getFile(projectId, path, branchName)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getRepositoryFiles(
        projectId: Long,
        path: String,
        branchName: String,
        recursive: Boolean? = null,
        page: Int,
        pageSize: Int = defaultPageSize
    ): Single<List<RepositoryFile>> =
        api
            .getRepositoryTree(projectId, path, branchName, recursive, page, pageSize)
            .flattenAsObservable { it }
            .flatMapSingle { treeNode ->
                if (treeNode.type == RepositoryTreeNodeType.BLOB) {
                    api.getFile(projectId, treeNode.path, branchName)
                        .flatMap { file ->
                            api.getRepositoryCommit(projectId, file.lastCommitId)
                                .map { commit ->
                                    RepositoryFile(
                                        treeNode.id,
                                        treeNode.name,
                                        RepositoryTreeNodeType.BLOB,
                                        commit.message,
                                        commit.authoredDate
                                    )
                                }
                        }
                } else {
                    api.getRepositoryCommits(projectId, branchName, null, null, treeNode.path, null, null)
                        .map { commits ->
                            val commit = commits.first()
                            RepositoryFile(
                                treeNode.id,
                                treeNode.name,
                                RepositoryTreeNodeType.TREE,
                                commit.message,
                                commit.authoredDate
                            )
                        }
                }
            }
            .toList()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}