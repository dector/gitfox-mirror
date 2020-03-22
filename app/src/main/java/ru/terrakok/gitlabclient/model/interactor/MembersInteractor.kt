package ru.terrakok.gitlabclient.model.interactor

import kotlinx.coroutines.flow.Flow
import ru.terrakok.gitlabclient.entity.Member
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges

/**
 * @author Valentin Logvinovitch (glvvl) on 27.02.19.
 */
class MembersInteractor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val defaultPageSize: Int
) {
    val memberChanges: Flow<Long> = serverChanges.memberChanges

    suspend fun getMembers(
        projectId: Long,
        page: Int,
        pageSize: Int = defaultPageSize
    ): List<Member> = api.getMembers(projectId, page, pageSize)

    suspend fun getMember(
        projectId: Long,
        memberId: Long
    ): Member = api.getMember(projectId, memberId)

    suspend fun addMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String? = null
    ) {
        api.addMember(projectId, userId, accessLevel, expiresDate)
    }

    suspend fun editMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String? = null
    ) {
        api.editMember(projectId, userId, accessLevel, expiresDate)
    }

    suspend fun deleteMember(
        projectId: Long,
        userId: Long
    ) {
        api.deleteMember(projectId, userId)
    }
}
