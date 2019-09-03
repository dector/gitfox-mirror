package ru.terrakok.gitlabclient.presentation.global

import android.annotation.SuppressLint
import com.jakewharton.rxrelay2.PublishRelay
import ru.terrakok.cicerone.Router
import ru.terrakok.gitlabclient.R
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.model.data.server.ServerError
import ru.terrakok.gitlabclient.model.data.server.TokenInvalidError
import ru.terrakok.gitlabclient.model.interactor.SessionInteractor
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.model.system.message.SystemMessageNotifier
import ru.terrakok.gitlabclient.util.userMessage
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.11.17.
 */
class ErrorHandler @Inject constructor(
    private val router: Router,
    private val sessionInteractor: SessionInteractor,
    private val systemMessageNotifier: SystemMessageNotifier,
    private val resourceManager: ResourceManager,
    private val schedulers: SchedulersProvider
) {

    private val authErrorRelay = PublishRelay.create<Boolean>()

    init {
        subscribeOnAuthErrors()
    }

    fun proceed(error: Throwable, messageListener: (String) -> Unit = {}) {
        Timber.e(error)
        when (error) {
            is ServerError -> when (error.errorCode) {
                401 -> authErrorRelay.accept(true)
                else -> messageListener(error.userMessage(resourceManager))
            }
            is TokenInvalidError -> authErrorRelay.accept(true)
            else -> messageListener(error.userMessage(resourceManager))
        }
    }

    @SuppressLint("CheckResult")
    private fun subscribeOnAuthErrors() {
        authErrorRelay
            .throttleFirst(50, TimeUnit.MILLISECONDS)
            .observeOn(schedulers.ui())
            .subscribe { logout() }
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
