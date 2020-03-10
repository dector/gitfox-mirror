package ru.terrakok.gitlabclient.model.interactor

import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.rx2.rxCompletable
import kotlinx.coroutines.rx2.rxSingle
import ru.terrakok.gitlabclient.di.DefaultPageSize
import ru.terrakok.gitlabclient.di.PrimitiveWrapper
import ru.terrakok.gitlabclient.entity.Member
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.state.ServerChanges
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

/**
 * @author Valentin Logvinovitch (glvvl) on 27.02.19.
 */
class MembersInteractor @Inject constructor(
    private val api: GitlabApi,
    serverChanges: ServerChanges,
    private val schedulers: SchedulersProvider,
    @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) {

    private val defaultPageSize = defaultPageSizeWrapper.value

    val memberChanges = serverChanges.memberChanges

    fun getMembers(
        projectId: Long,
        page: Int,
        pageSize: Int = defaultPageSize
    ): Single<List<Member>> =
        rxSingle { api.getMembers(projectId, page, pageSize) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun getMember(
        projectId: Long,
        memberId: Long
    ): Single<Member> =
        rxSingle { api.getMember(projectId, memberId) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun addMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String? = null
    ): Completable =
        rxCompletable { api.addMember(projectId, userId, accessLevel, expiresDate) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun editMember(
        projectId: Long,
        userId: Long,
        accessLevel: Long,
        expiresDate: String? = null
    ): Completable =
        rxCompletable { api.editMember(projectId, userId, accessLevel, expiresDate) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    fun deleteMember(
        projectId: Long,
        userId: Long
    ): Completable =
        rxCompletable { api.deleteMember(projectId, userId) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}
