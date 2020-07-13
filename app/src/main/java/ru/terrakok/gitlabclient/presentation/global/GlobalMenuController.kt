package ru.terrakok.gitlabclient.presentation.global

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 24.07.17.
 */
class GlobalMenuController {
    private val stateChannel = BroadcastChannel<Boolean>(1)

    val state: Flow<Boolean> = stateChannel.asFlow()
    fun open() = stateChannel.sendBlocking(true)
    fun close() = stateChannel.sendBlocking(false)
}
