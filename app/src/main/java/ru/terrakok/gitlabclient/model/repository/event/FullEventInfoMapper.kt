package ru.terrakok.gitlabclient.model.repository.event

import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.app.EventTarget
import ru.terrakok.gitlabclient.entity.app.FullEventInfo
import ru.terrakok.gitlabclient.entity.event.Event

/**
 * Created by Eugene Shapovalov (@CraggyHaggy) on 03.12.17.
 */
class FullEventInfoMapper {

    fun transform(event: Event, project: Project): FullEventInfo {
        return FullEventInfo(
                event.actionName,
                EventTarget.ISSUE,
                event.author,
                event.createdAt,
                project,
                "Test body",
                event.targetId ?: event.projectId
        )
    }
}