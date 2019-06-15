package ru.terrakok.gitlabclient.model.interactor.event

import javax.inject.Inject
import ru.terrakok.gitlabclient.model.repository.event.EventRepository

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17.
 */
class EventInteractor @Inject constructor(
    private val eventRepository: EventRepository
) {
    fun getEvents(page: Int) =
        eventRepository.getEvents(page = page)

    fun getProjectEvents(projectId: Long, page: Int) =
        eventRepository.getProjectEvents(projectId = projectId, page = page)
}