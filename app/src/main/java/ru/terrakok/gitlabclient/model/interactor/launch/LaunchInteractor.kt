package ru.terrakok.gitlabclient.model.interactor.launch

import ru.terrakok.gitlabclient.model.repository.app.AppInfoRepository
import ru.terrakok.gitlabclient.model.repository.launch.LaunchRepository
import ru.terrakok.gitlabclient.toothpick.DI
import ru.terrakok.gitlabclient.toothpick.module.ServerModule
import toothpick.Toothpick
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 26.09.18.
 */
class LaunchInteractor @Inject constructor(
    private val appInfoRepository: AppInfoRepository,
    private val launchRepository: LaunchRepository
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

    fun signInToSession(): Boolean {
        Toothpick.closeScope(DI.SERVER_SCOPE)
        Toothpick
            .openScopes(DI.APP_SCOPE, DI.SERVER_SCOPE)
            .installModules(ServerModule(launchRepository.getSelectedServerPath()))
        return launchRepository.isSignedIn
    }
}