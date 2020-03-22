package ru.terrakok.gitlabclient.model.data.state

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.asFlow

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class ServerChanges {

    private val issueChannel = BroadcastChannel<Long>(1)
    private val mergeRequestChannel = BroadcastChannel<Long>(1)
    private val projectChannel = BroadcastChannel<Long>(1)
    private val labelChannel = BroadcastChannel<Long>(1)
    private val milestoneChannel = BroadcastChannel<Long>(1)
    private val todoChannel = BroadcastChannel<Long>(1)
    private val userChannel = BroadcastChannel<Long>(1)
    private val memberChannel = BroadcastChannel<Long>(1)

    val issueChanges = issueChannel.asFlow()
    val mergeRequestChanges = mergeRequestChannel.asFlow()
    val projectChanges = projectChannel.asFlow()
    val labelChanges = labelChannel.asFlow()
    val milestoneChanges = milestoneChannel.asFlow()
    val todoChanges = todoChannel.asFlow()
    val userChanges = userChannel.asFlow()
    val memberChanges = memberChannel.asFlow()

    fun issueChanged(id: Long = -1) {
        issueChannel.sendBlocking(id)
    }

    fun mergeRequestChanged(id: Long = -1) {
        mergeRequestChannel.sendBlocking(id)
    }

    fun projectChanged(id: Long = -1) {
        projectChannel.sendBlocking(id)
    }

    fun labelChanged(id: Long = -1) {
        labelChannel.sendBlocking(id)
    }

    fun milestoneChanged(id: Long = -1) {
        milestoneChannel.sendBlocking(id)
    }

    fun todoChanged(id: Long = -1) {
        todoChannel.sendBlocking(id)
    }

    fun userChanged(id: Long = -1) {
        userChannel.sendBlocking(id)
    }

    fun memberChanged(id: Long = -1) {
        memberChannel.sendBlocking(id)
    }
}
