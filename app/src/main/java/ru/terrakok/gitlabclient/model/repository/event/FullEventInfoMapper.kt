package ru.terrakok.gitlabclient.model.repository.event

import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.app.EventTarget
import ru.terrakok.gitlabclient.entity.app.FullEventInfo
import ru.terrakok.gitlabclient.entity.event.Event
import ru.terrakok.gitlabclient.entity.event.EventAction
import ru.terrakok.gitlabclient.entity.event.EventTargetType

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 03.12.17.
 */
class FullEventInfoMapper {

    fun transform(event: Event, project: Project): FullEventInfo {
        return FullEventInfo(
                event.actionName,
                transformEventTarget(event),
                event.author,
                event.createdAt,
                project,
                "Test body",
                event.targetId ?: event.projectId
        )
    }

    private fun transformEventTarget(event: Event): EventTarget {
        return if (event.targetType != null) {
            if (event.targetType == EventTargetType.NOTE) {
                when (event.note!!.noteableType) {
                    EventTargetType.ISSUE -> EventTarget.ISSUE
                    EventTargetType.MERGE_REQUEST -> EventTarget.MERGE_REQUEST
                    EventTargetType.MILESTONE -> EventTarget.MILESTONE
                    EventTargetType.SNIPPET -> EventTarget.SNIPPET
                    else -> throw IllegalArgumentException(
                            "Unsupported noteable target type: ${event.note.noteableType}.")
                }
            } else {
                when (event.targetType) {
                    EventTargetType.ISSUE -> EventTarget.ISSUE
                    EventTargetType.MERGE_REQUEST -> EventTarget.MERGE_REQUEST
                    EventTargetType.MILESTONE -> EventTarget.MILESTONE
                    else -> throw IllegalArgumentException(
                            "Unsupported event target type: ${event.targetType}.")
                }
            }
        } else {
            when {
                event.actionName == EventAction.JOINED -> EventTarget.PROJECT
                event.pushData != null -> EventTarget.BRANCH
                else -> throw IllegalArgumentException(
                        "Unsupported event action name: ${event.actionName}.")
            }
        }
    }
}