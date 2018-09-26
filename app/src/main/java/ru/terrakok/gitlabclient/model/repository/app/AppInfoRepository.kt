package ru.terrakok.gitlabclient.model.repository.app

import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.app.develop.AppInfo
import ru.terrakok.gitlabclient.model.data.storage.Prefs
import ru.terrakok.gitlabclient.model.data.storage.RawAppData
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.12.17.
 */
class AppInfoRepository @Inject constructor(
    private val rawAppData: RawAppData,
    private val appInfo: AppInfo,
    private val prefs: Prefs,
    private val schedulers: SchedulersProvider
) {

    var firstLaunchTimeStamp: Long?
        get() = prefs.firstLaunchTimeStamp
        set(value) {
            prefs.firstLaunchTimeStamp = value
        }

    fun getAppInfo() = Single.just(appInfo)

    fun getAppLibraries() = rawAppData
        .getAppLibraries()
        .onErrorReturn { emptyList() }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())

    fun getAppDevelopers() = rawAppData
        .getAppDevelopers()
        .onErrorReturn { emptyList() }
        .subscribeOn(schedulers.io())
        .observeOn(schedulers.ui())
}