package ru.terrakok.gitlabclient.model.interactor.members

import ru.terrakok.gitlabclient.model.repository.members.MembersRepository
import javax.inject.Inject

/**
 * @author Valentin Logvinovitch (glvvl) on 27.02.19.
 */
class MembersInteractor @Inject constructor(
    private val membersRepository: MembersRepository
) {

    val memberChanges = membersRepository.memberChanges

    fun getMembers(projectId: Long, page: Int) =
            membersRepository.getMembers(projectId, page)

    fun getMember(projectId: Long, memberId: Long) =
            membersRepository.getMember(projectId, memberId)

    fun addMember(projectId: Long, userId: Long, accessLevel: Long) =
            membersRepository.addMember(projectId, userId, accessLevel)

    fun editMember(projectId: Long, userId: Long, accessLevel: Long) =
            membersRepository.editMember(projectId, userId, accessLevel)

    fun deleteMember(projectId: Long, userId: Long) =
            membersRepository.deleteMember(projectId, userId)
}