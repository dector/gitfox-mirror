package ru.terrakok.gitlabclient.model.interactor.event

import ru.terrakok.gitlabclient.model.repository.event.EventRepository
import javax.inject.Inject

/**
 * Created by Konstantin Tskhovrebov (aka @terrakok) on 22.07.17.
 */
class EventInteractor @Inject constructor(
    private val eventRepository: EventRepository
) {
    fun getEvents(page: Int) = eventRepository.getEvents(page = page)
}