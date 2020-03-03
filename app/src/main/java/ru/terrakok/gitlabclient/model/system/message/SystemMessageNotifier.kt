package ru.terrakok.gitlabclient.model.system.message

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.09.18.
 */
class SystemMessageNotifier {
    private val notifierRelay = PublishRelay.create<SystemMessage>()

    val notifier: Observable<SystemMessage> = notifierRelay.hide()
    fun send(message: SystemMessage) = notifierRelay.accept(message)
    fun send(message: String) = notifierRelay.accept(SystemMessage(message))
}
