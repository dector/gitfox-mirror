package ru.terrakok.gitlabclient.model.interactor

import javax.inject.Inject
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 25.11.17.
 */
class UserInteractor @Inject constructor(
    private val api: GitlabApi,
    private val schedulers: SchedulersProvider
) {

    fun getUser(id: Long) = api
        .getUser(id)
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
}
