package ru.terrakok.gitlabclient.markwonx

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class MarkdownClickMediator {
    data class MarkdownClickEvent(val extension: GitlabMarkdownExtension, val value: Any)

    private val events by lazy { BroadcastChannel<MarkdownClickEvent>(1) }

    fun markdownClicked(extension: GitlabMarkdownExtension, value: Any) {
        events.sendBlocking(MarkdownClickEvent(extension, value))
    }

    fun getClickEvents(): Flow<MarkdownClickEvent> = events.asFlow()
}