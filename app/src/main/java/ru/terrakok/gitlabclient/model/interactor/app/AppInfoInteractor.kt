package ru.terrakok.gitlabclient.model.interactor.app

import javax.inject.Inject
import ru.terrakok.gitlabclient.model.repository.app.AppInfoRepository

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.12.17.
 */
class AppInfoInteractor @Inject constructor(
    private val appInfoRepository: AppInfoRepository
) {

    fun getAppInfo() = appInfoRepository.getAppInfo()
    fun getAppLibraries() = appInfoRepository.getAppLibraries()
}