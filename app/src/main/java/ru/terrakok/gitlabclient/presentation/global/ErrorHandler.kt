package ru.terrakok.gitlabclient.presentation.global

import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.userMessage
import ru.terrakok.gitlabclient.model.data.server.ServerError
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.model.system.ResourceManager
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.11.17.
 */
class ErrorHandler @Inject constructor(
        private val router: Router,
        private val authInteractor: AuthInteractor,
        private val resourceManager: ResourceManager
) {

    fun proceed(error: Throwable, messageListener: (String) -> Unit = {}) {
        Timber.e("Error: $error")
        if (error is ServerError) {
            when (error.errorCode) {
                401 -> {
                    authInteractor.logout()
                    router.newRootScreen(Screens.AUTH_SCREEN)
                }
            }
        } else {
            messageListener.invoke(error.userMessage(resourceManager))
        }
    }
}
