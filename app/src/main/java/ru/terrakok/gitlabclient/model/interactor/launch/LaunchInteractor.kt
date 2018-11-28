package ru.terrakok.gitlabclient.model.interactor.launch

import ru.terrakok.gitlabclient.model.repository.app.AppInfoRepository
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 26.09.18.
 */
class LaunchInteractor @Inject constructor(
    private val appInfoRepository: AppInfoRepository
) {
    val isFirstLaunch: Boolean
        get() {
            val timeStamp = appInfoRepository.firstLaunchTimeStamp
            if (timeStamp == null) {
                appInfoRepository.firstLaunchTimeStamp = System.currentTimeMillis()
                return true
            } else {
                return false
            }
        }
}