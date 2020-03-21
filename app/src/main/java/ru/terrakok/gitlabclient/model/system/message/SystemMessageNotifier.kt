package ru.terrakok.gitlabclient.model.system.message

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 03.09.18.
 */
class SystemMessageNotifier {
    private val notifierChannel = BroadcastChannel<SystemMessage>(1)

    val notifier: Flow<SystemMessage> = notifierChannel.asFlow()
    fun send(message: SystemMessage) = notifierChannel.sendBlocking(message)
    fun send(message: String) = notifierChannel.sendBlocking(SystemMessage(message))
}
