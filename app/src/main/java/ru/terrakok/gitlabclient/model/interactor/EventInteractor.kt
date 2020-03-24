package ru.terrakok.gitlabclient.model.interactor

import ru.terrakok.gitlabclient.entity.*
import ru.terrakok.gitlabclient.entity.app.target.*
import ru.terrakok.gitlabclient.model.data.server.GitlabApi
import ru.terrakok.gitlabclient.model.data.server.MarkDownUrlResolver

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17.
 */
class EventInteractor(
    private val api: GitlabApi,
    private val defaultPageSize: Int,
    private val markDownUrlResolver: MarkDownUrlResolver
) {

    suspend fun getEvents(
        action: EventAction? = null,
        targetType: EventTarget? = null,
        beforeDay: Date? = null,
        afterDay: Date? = null,
        sort: Sort? = Sort.DESC,
        orderBy: OrderBy = OrderBy.UPDATED_AT,
        page: Int,
        pageSize: Int = defaultPageSize
    ): List<TargetHeader> {
        val sourceEvents = api.getEvents(
            action,
            targetType,
            beforeDay,
            afterDay,
            sort,
            orderBy,
            EventScope.ALL,
            page,
            pageSize
        ).apply { filterBrokenEvents(this) }
        val projects = getDistinctProjects(sourceEvents)
        return sourceEvents.map { getTargetHeader(it, projects[it.projectId]) }
    }

    suspend fun getProjectEvents(
        projectId: Long,
        action: EventAction? = null,
        targetType: EventTarget? = null,
        beforeDay: Date? = null,
        afterDay: Date? = null,
        sort: Sort? = Sort.DESC,
        orderBy: OrderBy = OrderBy.UPDATED_AT,
        page: Int,
        pageSize: Int = defaultPageSize
    ): List<TargetHeader> {

        val sourceEvents = api.getProjectEvents(
            projectId,
            action,
            targetType,
            beforeDay,
            afterDay,
            sort,
            orderBy,
            page,
            pageSize
        ).apply { filterBrokenEvents(this) }
        val projects = getDistinctProjects(sourceEvents)
        return sourceEvents.map { getTargetHeader(it, projects[it.projectId]) }
    }

    private fun filterBrokenEvents(events: List<Event>): List<Event> =
        events.filter { !isNoteBroken(it) }

    /**
     * Sometimes GitLab returns event with targetType = DIFF_NOTE || targetType = _NOTE, but _note = null.
     * On web these events are filtered.
     */
    private fun isNoteBroken(event: Event) =
        event.targetType != null &&
                (event.targetType == EventTargetType.DIFF_NOTE || event.targetType == EventTargetType.NOTE) &&
                event.note == null

    private suspend fun getDistinctProjects(events: List<Event>): Map<Long, Project> =
        events.filter { it.projectId != 0L }
            .distinctBy { it.projectId }
            .associate { it.projectId to api.getProject(it.projectId) }


    private fun getTargetHeader(event: Event, project: Project?): TargetHeader {
        // There are two event types: public and confidential.
        // Public events are opened to read with any rights in project.
        // Confidential event is closed to read without specific rights in project.
        // So GitLab returns it will undefined values.
        return if (event.projectId != 0L) {
            val targetData = getTarget(event)
            val badges = mutableListOf<TargetBadge>()
            project?.let { badges.add(TargetBadge.Text(it.name, AppTarget.PROJECT, it.id)) }
            badges.add(TargetBadge.Text(event.author.username, AppTarget.USER, event.author.id))

            TargetHeader.Public(
                event.author,
                getIcon(event.actionName),
                TargetHeaderTitle.Event(
                    event.author.name,
                    event.actionName,
                    targetData.name,
                    project?.name ?: ""
                ),
                getBody(event, project) ?: "",
                event.createdAt,
                targetData.target,
                targetData.id,
                getTargetInternal(event),
                badges,
                getTargetAction(event)
            )
        } else {
            TargetHeader.Confidential
        }
    }

    private fun getIcon(action: EventAction) = when (action) {
        EventAction.CREATED -> TargetHeaderIcon.CREATED
        EventAction.IMPORTED -> TargetHeaderIcon.IMPORTED
        EventAction.JOINED -> TargetHeaderIcon.JOINED
        EventAction.COMMENTED_ON,
        EventAction.COMMENTED ->
            TargetHeaderIcon.COMMENTED
        EventAction.MERGED,
        EventAction.ACCEPTED ->
            TargetHeaderIcon.MERGED
        EventAction.CLOSED -> TargetHeaderIcon.CLOSED
        EventAction.DELETED,
        EventAction.DESTROYED ->
            TargetHeaderIcon.DESTROYED
        EventAction.EXPIRED -> TargetHeaderIcon.EXPIRED
        EventAction.LEFT -> TargetHeaderIcon.LEFT
        EventAction.OPENED,
        EventAction.REOPENED ->
            TargetHeaderIcon.REOPENED
        EventAction.PUSHED,
        EventAction.PUSHED_NEW,
        EventAction.PUSHED_TO ->
            TargetHeaderIcon.PUSHED
        EventAction.UPDATED -> TargetHeaderIcon.UPDATED
    }

    private fun getTarget(event: Event): TargetData =
        when (event.targetType) {
            EventTargetType.ISSUE -> TargetData(
                AppTarget.ISSUE,
                "${AppTarget.ISSUE} #${event.targetIid!!}",
                event.targetId!!
            )
            EventTargetType.MERGE_REQUEST -> TargetData(
                AppTarget.MERGE_REQUEST,
                "${AppTarget.MERGE_REQUEST} !${event.targetIid!!}",
                event.targetId!!
            )
            EventTargetType.MILESTONE -> TargetData(
                AppTarget.MILESTONE,
                "${AppTarget.MILESTONE} ${event.targetIid!!}",
                event.targetId!!
            )
            EventTargetType.SNIPPET -> TargetData(
                AppTarget.SNIPPET,
                "${AppTarget.SNIPPET} ${event.targetIid!!}",
                event.targetId!!
            )
            EventTargetType.COMMIT -> TargetData(
                AppTarget.COMMIT,
                "${AppTarget.COMMIT} ${event.targetIid!!}",
                event.targetId!!
            )
            EventTargetType.DISCUSSION_NOTE -> TargetData(
                AppTarget.PROJECT,
                "${AppTarget.NOTE} ${event.targetIid!!}",
                event.projectId
            )
            EventTargetType.DIFF_NOTE,
            EventTargetType.NOTE -> {
                if (event.note != null) {
                    when (event.note.noteableType) {
                        EventTargetType.ISSUE -> TargetData(
                            AppTarget.ISSUE,
                            "${AppTarget.ISSUE} #${event.note.noteableIid}",
                            event.note.noteableId ?: event.note.id
                        )
                        EventTargetType.MERGE_REQUEST -> TargetData(
                            AppTarget.MERGE_REQUEST,
                            "${AppTarget.MERGE_REQUEST} !${event.note.noteableIid}",
                            event.note.noteableId ?: event.note.id
                        )
                        EventTargetType.MILESTONE -> TargetData(
                            AppTarget.MILESTONE,
                            "${AppTarget.MILESTONE} ${event.note.noteableIid}",
                            event.note.noteableId ?: event.note.id
                        )
                        EventTargetType.SNIPPET -> TargetData(
                            AppTarget.SNIPPET,
                            "${AppTarget.SNIPPET} ${event.note.noteableIid}",
                            event.note.noteableId ?: event.note.id
                        )
                        EventTargetType.COMMIT, null -> TargetData(
                            AppTarget.COMMIT,
                            "${AppTarget.COMMIT} ${event.note.id}",
                            event.note.id
                        )
                        else -> throw IllegalArgumentException("Unsupported noteable target type: ${event.note.noteableType}.")
                    }
                } else {
                    throw IllegalArgumentException("Unsupported event type: $event.")
                }
            }
            else -> {
                if (event.pushData != null) {
                    when (event.pushData.refType) {
                        PushDataRefType.BRANCH -> TargetData(
                            AppTarget.PROJECT,
                            "${AppTarget.BRANCH} ${event.pushData.ref}",
                            event.projectId
                        )
                        PushDataRefType.TAG -> TargetData(
                            AppTarget.PROJECT,
                            "${AppTarget.TAG} ${event.pushData.ref}",
                            event.projectId
                        )
                    }
                } else if (event.actionName == EventAction.IMPORTED) {
                    TargetData(
                        AppTarget.PROJECT,
                        AppTarget.PROJECT.toString(),
                        event.projectId
                    )
                } else {
                    TargetData(
                        AppTarget.PROJECT,
                        "${AppTarget.PROJECT} ${event.projectId}",
                        event.projectId
                    )
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

    private fun getTargetAction(event: Event): TargetAction =
        when (event.actionName) {
            EventAction.COMMENTED_ON -> {
                event.note
                    ?.id
                    ?.let { TargetAction.CommentedOn(it) }
                    ?: TargetAction.Undefined
            }
            else -> TargetAction.Undefined
        }

    private fun getBody(event: Event, project: Project?) = when (event.targetType) {
        EventTargetType.NOTE,
        EventTargetType.DIFF_NOTE -> {
            if (event.note != null && project != null) {
                markDownUrlResolver.resolve(event.note.body, project)
            } else {
                null
            }
        }
        EventTargetType.ISSUE,
        EventTargetType.MERGE_REQUEST,
        EventTargetType.MILESTONE ->
            event.targetTitle
        else -> event.pushData?.commitTitle
    }

    private data class TargetData(
        val target: AppTarget,
        val name: String,
        val id: Long
    )
}
