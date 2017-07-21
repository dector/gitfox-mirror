package ru.terrakok.gitlabclient.model.repository.event

import ru.terrakok.gitlabclient.entity.EventAction
import ru.terrakok.gitlabclient.entity.EventTarget
import ru.terrakok.gitlabclient.entity.Sort
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
    private val dayFormat = SimpleDateFormat("YYYY-MM-DD")

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
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.newThread())
}