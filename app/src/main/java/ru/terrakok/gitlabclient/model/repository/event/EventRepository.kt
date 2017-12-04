package ru.terrakok.gitlabclient.model.repository.event

import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.Sort
import ru.terrakok.gitlabclient.entity.app.FullEventInfo
import ru.terrakok.gitlabclient.entity.app.FullEventTarget
import ru.terrakok.gitlabclient.entity.event.Event
import ru.terrakok.gitlabclient.entity.event.EventAction
import ru.terrakok.gitlabclient.entity.event.EventTarget
import ru.terrakok.gitlabclient.entity.event.EventTargetType
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultPageSize
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17.
 */
class EventRepository @Inject constructor(
        private val api: GitlabApi,
        private val schedulers: SchedulersProvider,
        @DefaultPageSize private val defaultPageSizeWrapper: PrimitiveWrapper<Int>
) {
    private val defaultPageSize = defaultPageSizeWrapper.value
    private val dayFormat = SimpleDateFormat("yyyy-MM-dd")

    private val cachedProjects = mutableMapOf<Long, Project>()

    fun getEvents(
            action: EventAction? = null,
            targetType: EventTarget? = null,
            beforeDay: Date? = null,
            afterDay: Date? = null,
            sort: Sort? = null,
            page: Int,
            pageSize: Int = defaultPageSize
    ) = api
            .getEvents(
                    action,
                    targetType,
                    beforeDay?.run { dayFormat.format(this) },
                    afterDay?.run { dayFormat.format(this) },
                    sort,
                    page,
                    pageSize
            )
            .flattenAsObservable { it }
            .flatMapSingle { event ->
                val projectId = event.projectId
                if (cachedProjects.containsKey(projectId)) {
                    Single.just(getFullEventInfo(event, cachedProjects[projectId]!!))
                } else {
                    api.getProject(projectId)
                            .doOnSuccess { cachedProjects.put(projectId, it) }
                            .map { project -> getFullEventInfo(event, project) }
                }
            }
            .toList()
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    private fun getFullEventInfo(event: Event, project: Project): FullEventInfo {
        return FullEventInfo(
                event.actionName,
                getFullEventTarget(event),
                event.author,
                event.createdAt,
                project,
                getBody(event),
                event.targetId ?: event.projectId
        )
    }

    private fun getFullEventTarget(event: Event): FullEventTarget {
        return if (event.targetType != null) {
            when (event.targetType) {
                EventTargetType.ISSUE -> FullEventTarget.ISSUE
                EventTargetType.MERGE_REQUEST -> FullEventTarget.MERGE_REQUEST
                EventTargetType.MILESTONE -> FullEventTarget.MILESTONE
                EventTargetType.NOTE -> {
                    when (event.note!!.noteableType) {
                        EventTargetType.ISSUE -> FullEventTarget.ISSUE
                        EventTargetType.MERGE_REQUEST -> FullEventTarget.MERGE_REQUEST
                        EventTargetType.MILESTONE -> FullEventTarget.MILESTONE
                        EventTargetType.SNIPPET -> FullEventTarget.SNIPPET
                        else -> throw IllegalArgumentException(
                                "Unsupported noteable target type: ${event.note.noteableType}.")
                    }
                }
                else -> throw IllegalArgumentException(
                        "Unsupported event target type: ${event.targetType}.")
            }
        } else {
            when {
                event.actionName == EventAction.JOINED -> FullEventTarget.PROJECT
                event.pushData != null -> FullEventTarget.BRANCH
                else -> throw IllegalArgumentException(
                        "Unsupported event action name: ${event.actionName}.")
            }
        }
    }

    private fun getBody(event: Event): String? {
        return if (event.targetType != null) {
            when (event.targetType) {
                EventTargetType.NOTE -> event.note!!.body
                EventTargetType.ISSUE -> event.targetTitle
                EventTargetType.MERGE_REQUEST -> event.targetTitle
                EventTargetType.MILESTONE -> event.targetTitle
                else -> null
            }
        } else {
            if (event.pushData != null) {
                event.pushData.commitTitle
            } else {
                null
            }
        }
    }
}