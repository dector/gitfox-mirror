package ru.terrakok.gitlabclient.model.interactor

import ru.terrakok.gitlabclient.entity.app.develop.AppInfo
import ru.terrakok.gitlabclient.entity.app.develop.AppLibrary
import ru.terrakok.gitlabclient.model.data.storage.RawAppData

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.12.17.
 */
class AppInfoInteractor(
    private val rawAppData: RawAppData,
    private val appInfo: AppInfo
) {

    fun getAppInfo(): AppInfo = appInfo

    suspend fun getAppLibraries(): List<AppLibrary> = try {
        rawAppData.getAppLibraries()
    } catch (e: Exception) {
        emptyList()
    }
}
