package ru.terrakok.gitlabclient.model.interactor

import ru.terrakok.gitlabclient.entity.app.develop.AppInfo
import ru.terrakok.gitlabclient.entity.app.develop.AppLibrary

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.12.17.
 */
class AppInfoInteractor(
    private val getLibraries: suspend () -> List<AppLibrary>,
    private val appInfo: AppInfo
) {

    fun getAppInfo(): AppInfo = appInfo

    suspend fun getAppLibraries(): List<AppLibrary> = try {
        getLibraries()
    } catch (e: Exception) {
        emptyList()
    }
}
