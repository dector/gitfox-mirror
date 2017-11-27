package ru.terrakok.gitlabclient.entity.app

import ru.terrakok.gitlabclient.entity.Author
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.event.EventAction
import java.util.*

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 19.11.17.
 */
data class FullEventInfo(
        val action: EventAction,
        val target: EventTarget,
        val author: Author,
        val createdAt: Date,
        val project: Project?,
        val body: String?,
        val targetId: Long
)