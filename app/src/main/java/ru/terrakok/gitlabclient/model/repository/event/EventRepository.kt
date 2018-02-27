package ru.terrakok.gitlabclient.model.repository.event

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.threeten.bp.LocalDateTime
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.PushDataRefType
import ru.terrakok.gitlabclient.entity.Sort
import ru.terrakok.gitlabclient.entity.app.target.*
import ru.terrakok.gitlabclient.entity.event.Event
import ru.terrakok.gitlabclient.entity.event.EventAction
import ru.terrakok.gitlabclient.entity.event.EventTarget
import ru.terrakok.gitlabclient.entity.event.EventTargetType
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.system.SchedulersProvider
import ru.terrakok.gitlabclient.toothpick.PrimitiveWrapper
import ru.terrakok.gitlabclient.toothpick.qualifier.DefaultPageSize
import java.text.SimpleDateFormat
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
            beforeDay: LocalDateTime? = null,
            afterDay: LocalDateTime? = null,
            sort: Sort? = null,
            page: Int,
            pageSize: Int = defaultPageSize
    ): Single<List<TargetHeader>> = api
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
                        BiFunction<List<Event>, Map<Long, Project>, List<TargetHeader>> { sourceEvents, projects ->
                            sourceEvents.map { getTargetHeader(it, projects[it.projectId]) }
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

    private fun getTargetHeader(event: Event, project: Project?): TargetHeader {
        val targetData = getTarget(event)
        val badges = mutableListOf<TargetBadge>()
        project?.let { badges.add(TargetBadge.Text(it.name, AppTarget.PROJECT, it.id)) }
        badges.add(TargetBadge.Text(event.author.username, AppTarget.USER, event.author.id))
        event.pushData?.let { pushData ->
            badges.add(TargetBadge.Icon(TargetBadgeIcon.COMMITS, pushData.commitCount))
        }

        return TargetHeader(
                event.author,
                getIcon(event.actionName),
                TargetHeaderTitle.Event(
                        event.author.name,
                        event.actionName,
                        targetData.name,
                        project?.name ?: ""
                ),
                getBody(event),
                event.createdAt,
                targetData.target,
                targetData.id,
                getTargetInternal(event),
                badges
        )
    }

    private fun getIcon(action: EventAction) = when (action) {
        EventAction.CREATED -> TargetHeaderIcon.CREATED
        EventAction.JOINED -> TargetHeaderIcon.JOINED
        EventAction.COMMENTED_ON,
        EventAction.COMMENTED -> TargetHeaderIcon.COMMENTED
        EventAction.MERGED,
        EventAction.ACCEPTED -> TargetHeaderIcon.MERGED
        EventAction.CLOSED -> TargetHeaderIcon.CLOSED
        EventAction.DELETED,
        EventAction.DESTROYED -> TargetHeaderIcon.DESTROYED
        EventAction.EXPIRED -> TargetHeaderIcon.EXPIRED
        EventAction.LEFT -> TargetHeaderIcon.LEFT
        EventAction.OPENED,
        EventAction.REOPENED -> TargetHeaderIcon.REOPENED
        EventAction.PUSHED,
        EventAction.PUSHED_NEW,
        EventAction.PUSHED_TO -> TargetHeaderIcon.PUSHED
        EventAction.UPDATED -> TargetHeaderIcon.UPDATED
    }

    private fun getTarget(event: Event): TargetData =
            when (event.targetType) {
                EventTargetType.ISSUE -> TargetData(AppTarget.ISSUE, "${AppTarget.ISSUE} #${event.targetIid!!}", event.targetId!!)
                EventTargetType.MERGE_REQUEST -> TargetData(AppTarget.MERGE_REQUEST, "${AppTarget.MERGE_REQUEST} !${event.targetIid!!}", event.targetId!!)
                EventTargetType.MILESTONE -> TargetData(AppTarget.MILESTONE, "${AppTarget.MILESTONE} ${event.targetIid!!}", event.targetId!!)
                EventTargetType.SNIPPET -> TargetData(AppTarget.SNIPPET, "${AppTarget.SNIPPET} ${event.targetIid!!}", event.targetId!!)
                EventTargetType.DIFF_NOTE,
                EventTargetType.NOTE -> {
                    if (event.note != null) {
                        when (event.note.noteableType) {
                            EventTargetType.ISSUE -> TargetData(AppTarget.ISSUE, "${AppTarget.ISSUE} #${event.note.noteableIid}", event.note.noteableId)
                            EventTargetType.MERGE_REQUEST -> TargetData(AppTarget.MERGE_REQUEST, "${AppTarget.MERGE_REQUEST} !${event.note.noteableIid}", event.note.noteableId)
                            EventTargetType.MILESTONE -> TargetData(AppTarget.MILESTONE, "${AppTarget.MILESTONE} ${event.note.noteableIid}", event.note.noteableId)
                            EventTargetType.SNIPPET -> TargetData(AppTarget.SNIPPET, "${AppTarget.SNIPPET} ${event.note.noteableIid}", event.note.noteableId)
                            null -> TargetData(AppTarget.COMMIT, "${AppTarget.COMMIT} ${event.note.id}", event.note.id)
                            else -> throw IllegalArgumentException("Unsupported noteable target type: ${event.note.noteableType}.")
                        }
                    } else {
                        throw IllegalArgumentException("Unsupported event type: $event.")
                    }
                }
                else -> {
                    if (event.pushData != null) {
                        when (event.pushData.refType) {
                            PushDataRefType.BRANCH -> TargetData(AppTarget.PROJECT, "${AppTarget.BRANCH} ${event.pushData.ref}", event.projectId)
                            PushDataRefType.TAG -> TargetData(AppTarget.PROJECT, "${AppTarget.TAG} ${event.pushData.ref}", event.projectId)
                        }
                    } else {
                        TargetData(AppTarget.PROJECT, "${AppTarget.PROJECT} ${event.projectId}", event.projectId)
                    }
                }
            }

    private fun getTargetInternal(event: Event): TargetInternal? =
        when (event.targetType) {
            EventTargetType.DIFF_NOTE,
            EventTargetType.NOTE -> {
                if (event.note?.noteableIid != null) {
                    TargetInternal(event.projectId, event.note.noteableIid)
                } else if (event.targetIid != null) {
                    TargetInternal(event.projectId, event.targetIid)
                } else {
                    null
                }
            }
            else -> {
                if (event.targetIid != null) {
                    TargetInternal(event.projectId, event.targetIid)
                } else {
                    null
                }
            }
        }

    private fun getBody(event: Event) = when (event.targetType) {
        EventTargetType.NOTE,
        EventTargetType.DIFF_NOTE -> event.note?.body
        EventTargetType.ISSUE,
        EventTargetType.MERGE_REQUEST,
        EventTargetType.MILESTONE -> event.targetTitle
        else -> event.pushData?.commitTitle
    }

    private data class TargetData(
            val target: AppTarget,
            val name: String,
            val id: Long
    )
}