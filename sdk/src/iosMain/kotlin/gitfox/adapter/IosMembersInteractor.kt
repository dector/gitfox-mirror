package gitfox.adapter

import gitfox.entity.Member
import gitfox.model.interactor.MembersInteractor
import kotlinx.coroutines.CoroutineScope

class IosMembersInteractor internal constructor(
    private val interactor: MembersInteractor,
    private val defaultPageSize: Int
) : CoroutineScope by CoroutineScope(MainLoopDispatcher) {

    val memberChanges = interactor.memberChanges.wrap()

    fun getMembers(
        projectId: Long,
        page: Int,
        pageSize: Int = defaultPageSize,
        callback: (result: List<Member>?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.getMembers(projectId, page, pageSize) }
    }

    fun getMember(
        projectId: Long,
        memberId: Long,
        callback: (result: Member?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.getMember(projectId, memberId) }
    }

    fun addMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String? = null,
        callback: (result: Unit?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.addMember(projectId, userId, accessLevel, expiresDate) }
    }

    fun editMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String? = null,
        callback: (result: Unit?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.editMember(projectId, userId, accessLevel, expiresDate) }
    }

    fun deleteMember(
        projectId: Long,
        userId: Long,
        callback: (result: Unit?, error: Exception?) -> Unit
    ) {
        wrap(callback) { interactor.deleteMember(projectId, userId) }
    }
}