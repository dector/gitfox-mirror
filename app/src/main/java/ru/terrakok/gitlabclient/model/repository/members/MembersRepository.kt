package ru.terrakok.gitlabclient.model.repository.members

import io.reactivex.Completable
import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.Member
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultPageSize
import javax.inject.Inject

/**
 * @author Valentin Logvinovitch (glvvl) on 27.02.19.
 */
class MembersRepository @Inject constructor(
    private val api: GitlabApi,
    private val schedulers: SchedulersProvider,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) {

    private val defaultPageSize = defaultPageSizeWrapper.value

    fun getMembers(
        projectId: Long,
        page: Int,
        pageSize: Int = defaultPageSize
    ): Single<List<Member>> =
            api
                    .getMembers(projectId, page, pageSize)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())

    fun getMember(
        projectId: Long,
        memberId: Long
    ): Single<Member> =
            api
                    .getMember(projectId, memberId)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())

    fun addMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String? = null
    ): Completable =
            api
                    .addMember(projectId, userId, accessLevel, expiresDate)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())

    fun editMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String? = null
    ): Completable =
            api
                    .editMember(projectId, userId, accessLevel, expiresDate)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())

    fun deleteMember(
        projectId: Long,
        userId: Long
    ): Completable =
            api
                    .deleteMember(projectId, userId)
                    .subscribeOn(schedulers.io())
                    .observeOn(schedulers.ui())
}