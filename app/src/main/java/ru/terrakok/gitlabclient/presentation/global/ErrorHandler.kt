package ru.terrakok.gitlabclient.presentation.global

import com.jakewharton.rxrelay2.PublishRelay
import ru.terrakok.gitlabclient.Screens
import ru.terrakok.gitlabclient.extension.userMessage
import ru.terrakok.gitlabclient.model.data.server.ServerError
import ru.terrakok.gitlabclient.model.interactor.auth.AuthInteractor
import ru.terrakok.gitlabclient.model.system.ResourceManager
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.model.system.flow.FlowRouter
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.11.17.
 */
class ErrorHandler @Inject constructor(
    private val router: FlowRouter,
    private val authInteractor: AuthInteractor,
    private val resourceManager: ResourceManager,
    private val schedulers: SchedulersProvider
) {

    private val authErrorRelay = PublishRelay.create<Boolean>()

    init {
        subscribeOnAuthErrors()
    }

    fun proceed(error: Throwable, messageListener: (String) -> Unit = {}) {
        Timber.e(error)
        if (error is ServerError) {
            when (error.errorCode) {
                401 -> authErrorRelay.accept(true)
                else -> messageListener(error.userMessage(resourceManager))
            }
        } else {
            messageListener(error.userMessage(resourceManager))
        }
    }

    private fun subscribeOnAuthErrors() {
        authErrorRelay
            .throttleFirst(50, TimeUnit.MILLISECONDS)
            .observeOn(schedulers.ui())
            .subscribe { logout() }
    }

    private fun logout() {
        authInteractor.logout()
        router.startFlow(Screens.AUTH_FLOW)
    }
}
