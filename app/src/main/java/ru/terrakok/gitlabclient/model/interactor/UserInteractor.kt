package ru.terrakok.gitlabclient.model.interactor

import kotlinx.coroutines.rx2.rxSingle
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
class UserInteractor @Inject constructor(
    private val api: GitlabApi,
    private val schedulers: SchedulersProvider
) {

    fun getUser(id: Long) =
        rxSingle { api.getUser(id) }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}
