package ru.terrakok.gitlabclient.presentation.global

import android.annotation.SuppressLint
import com.github.aakira.napier.Napier
import gitfox.model.interactor.SessionInteractor
import io.ktor.client.features.ResponseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.system.ResourceManager
import ru.terrakok.gitlabclient.system.message.SystemMessageNotifier
import ru.terrakok.gitlabclient.util.e
import ru.terrakok.gitlabclient.util.userMessage
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.11.17.
 */
class ErrorHandler @Inject constructor(
    private val router: Router,
    private val sessionInteractor: SessionInteractor,
    private val systemMessageNotifier: SystemMessageNotifier,
    private val resourceManager: ResourceManager
) : CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val authErrorChannel = Channel<Boolean>()

    init {
        subscribeOnAuthErrors()
    }

    fun proceed(error: Throwable, messageListener: (String) -> Unit = {}) {
        Napier.e(error)
        when (error) {
            is ResponseException -> when (error.response.status.value) {
                401 -> launch { authErrorChannel.send(true) }
                else -> messageListener(error.userMessage(resourceManager))
            }
            else -> messageListener(error.userMessage(resourceManager))
        }
    }

    @SuppressLint("CheckResult")
    private fun subscribeOnAuthErrors() {
        launch {
            var lastErrorTime = 0L
            authErrorChannel.consumeEach {
                val current = System.currentTimeMillis()
                if (current - lastErrorTime > 50) {
                    logout()
                }
                lastErrorTime = current
            }
        }
    }

    private fun logout() {
        systemMessageNotifier.send(resourceManager.getString(R.string.auto_logout_message))
        val hasOtherAccount = sessionInteractor.logout()
        if (hasOtherAccount) {
            router.newRootScreen(Screens.DrawerFlow)
        } else {
            router.newRootScreen(Screens.AuthFlow)
        }
    }
}
