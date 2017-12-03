package ru.terrakok.gitlabclient.model.repository.event

import io.reactivex.Single
import ru.terrakok.gitlabclient.entity.Project
import ru.terrakok.gitlabclient.entity.Sort
import ru.terrakok.gitlabclient.entity.event.EventAction
import ru.terrakok.gitlabclient.entity.event.EventTarget
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
        private val fullEventInfoMapper: FullEventInfoMapper,
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
                    Single.just(fullEventInfoMapper.transform(event, cachedProjects[projectId]!!))
                } else {
                    api.getProject(projectId)
                            .doOnSuccess { cachedProjects.put(projectId, it) }
                            .map { project -> fullEventInfoMapper.transform(event, project) }
                }
            }
            .toList()
            .doOnEvent { _, _ -> cachedProjects.clear() }
            .subscribeOn(schedulers.io())
            .observeOn(schedulers.ui())
}