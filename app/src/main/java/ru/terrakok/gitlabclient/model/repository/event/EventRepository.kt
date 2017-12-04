package ru.terrakok.gitlabclient.model.repository.event

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
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
            .flatMap { events ->
                Single.zip(
                        Single.just(events),
                        getDistinctProjects(events),
                        BiFunction<List<Event>, Map<Long, Project>, List<FullEventInfo>>
                        { sourceEvents, projects ->
                            val fullEventInfos = mutableListOf<FullEventInfo>()
                            sourceEvents.forEach {
                                fullEventInfos.add(getFullEventInfo(it, projects[it.projectId]!!))
                            }
                            return@BiFunction fullEventInfos
                        }
                )
            }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())

    private fun getDistinctProjects(events: List<Event>): Single<Map<Long, Project>> {
        return Observable.fromIterable(events)
                .distinct { it.projectId }
                .flatMapSingle { event -> api.getProject(event.projectId) }
                .toMap { it.id }
    }

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