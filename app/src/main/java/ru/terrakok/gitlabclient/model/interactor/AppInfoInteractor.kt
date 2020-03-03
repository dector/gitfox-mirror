package ru.terrakok.gitlabclient.model.interactor

import io.reactivex.Single
import javax.inject.Inject
import ru.terrakok.gitlabclient.entity.app.develop.AppInfo
import ru.terrakok.gitlabclient.model.data.storage.RawAppData
import ru.terrakok.gitlabclient.model.system.SchedulersProvider

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.12.17.
 */
class AppInfoInteractor @Inject constructor(
    private val rawAppData: RawAppData,
    private val appInfo: AppInfo,
    private val schedulers: SchedulersProvider
) {

    fun getAppInfo() = Single.just(appInfo)

    fun getAppLibraries() = rawAppData
        .getAppLibraries()
        .onErrorReturn { emptyList() }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
}
