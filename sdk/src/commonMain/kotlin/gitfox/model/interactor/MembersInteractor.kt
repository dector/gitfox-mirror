package gitfox.model.interactor

import gitfox.entity.Member
import gitfox.model.data.server.GitlabApi
import gitfox.model.data.state.ServerChanges
import kotlinx.coroutines.flow.Flow

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
