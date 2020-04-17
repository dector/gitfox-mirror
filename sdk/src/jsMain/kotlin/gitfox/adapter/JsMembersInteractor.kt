package gitfox.adapter

import gitfox.model.interactor.MembersInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.promise

class JsMembersInteractor internal constructor(
    private val interactor: MembersInteractor,
    private val defaultPageSize: Int
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    @JsName("memberChanges")
    val memberChanges = interactor.memberChanges.wrap()

    @JsName("getMembers")
    fun getMembers(
        projectId: Long,
        page: Int,
        pageSize: Int = defaultPageSize
    ) = promise {
        interactor.getMembers(projectId, page, pageSize)
    }

    @JsName("getMember")
    fun getMember(
        projectId: Long,
        memberId: Long
    ) = promise {
        interactor.getMember(projectId, memberId)
    }

    @JsName("addMember")
    fun addMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String? = null
    ) = promise {
        interactor.addMember(projectId, userId, accessLevel, expiresDate)
    }

    @JsName("editMember")
    fun editMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String? = null
    ) = promise {
        interactor.editMember(projectId, userId, accessLevel, expiresDate)
    }

    @JsName("deleteMember")
    fun deleteMember(
        projectId: Long,
        userId: Long
    ) = promise {
        interactor.deleteMember(projectId, userId)
    }
}